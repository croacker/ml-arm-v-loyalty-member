package ru.peaksystems.varm.loyalty.component;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.CardOperation;
import ru.peak.ml.loyalty.core.data.Equipment;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peak.ml.loyalty.core.data.dao.CardOperationDao;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.domain.dto.CardOperationFilterParameters;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;
import ru.peaksystems.varm.loyalty.layout.LayoutCommand;
import ru.peaksystems.varm.loyalty.layout.MenuCommandsOwner;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public final class CardOperationsTable extends Table implements MenuCommandsOwner {

    protected static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        }
    };

    private static DecimalFormat moneyFormat = new DecimalFormat("0.00");

    private CardOperationFilterParameters cardOperationFilterParameters = new CardOperationFilterParameters();

    private CardOperationDao cardOperationDao;

    private Holder holder;

    public CardOperationFilterParameters getCardOperationFilterParameters(){
        return cardOperationFilterParameters;
    }

    public void setCardOperationFilterParameters(CardOperationFilterParameters cardOperationFilterParameters){
        this.cardOperationFilterParameters = cardOperationFilterParameters;
    }

    /**
     * Список имен атрибутов (entityFieldName) класса CardOperation для отображения
     */
    private static Map<String, String> columnTitles = new LinkedHashMap<>();

    static {
        columnTitles.put("referenceNumber", "Номер ссылки");
        columnTitles.put("operationTime", "Дата");
        columnTitles.put("equipment", "Магазин");
        columnTitles.put("loyaltyType", "Тип");
        columnTitles.put("sum", "Сумма");
        columnTitles.put("sumLoyalty", "Вознаграждение");
    }

    public CardOperationDao getCardOperationDao() {
        if (cardOperationDao == null) {
            cardOperationDao = GuiceConfigSingleton.inject(CardOperationDao.class);
        }
        return cardOperationDao;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    @Override
    protected String formatPropertyValue(final Object rowId,
                                         final Object colId, final Property<?> property) {
        if (colId.equals("comment")) {
            return StringUtil.EMPTY;
        }
        String result = super.formatPropertyValue(rowId, colId, property);
        if (colId.equals("sum") || colId.equals("sumLoyalty")) {
            if (property != null && property.getValue() != null) {
                Double r = Double.valueOf((Long) property.getValue());
                r = r / 100;
                result = moneyFormat.format(r);
            } else {
                result = "0,00";
            }
        } else if (colId.equals("operationTime")) {
            if (property != null && property.getValue() != null) {
                result = dateFormat.get().format(property.getValue());
            } else {
                result = StringUtil.EMPTY;
            }
        } else if (colId.equals("equipment")) {
            if (property != null && property.getValue() != null) {
                Equipment equipment = (Equipment) property.getValue();
                if (equipment.getShop() != null) {
                    result = equipment.getShop().getName();
                } else {
                    result = StringUtil.EMPTY;
                }
            } else {
                result = StringUtil.EMPTY;
            }
        }
        return result;
    }

    public CardOperationsTable() {
        setCaption("Операции по картам");

        addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        addStyleName(ValoTheme.TABLE_SMALL);
        setColumnAlignment("referenceNumber", Align.RIGHT);
        setRowHeaderMode(RowHeaderMode.INDEX);
        setSelectable(true);
        setMultiSelect(false);
        setSizeFull();

        setContainerDataSource(getDataContainer());

        setVisibleColumns(columnTitles.keySet().toArray());
        setColumnHeaders(columnTitles.values().toArray(new String[0]));

        setSortContainerPropertyId("operationTime");
        setSortAscending(false);

        addItemClickListener(itemClickEvent -> {
            if(itemClickEvent.isDoubleClick()) {
                CardOperation cardOperation = (CardOperation) ((BeanItem) itemClickEvent.getItem()).getBean();
                CardOperationDetailViewWindow.open(cardOperation);
            }
        });

        DashboardEventBus.register(this);
    }

    private Container getDataContainer() {
        return new BeanItemContainer<>(CardOperation.class);
    }

    /**
     * Обновить источник данных
     */
    public void updateDataContainer() {
        BeanItemContainer containerDataSource = (BeanItemContainer) getContainerDataSource();
        if (containerDataSource.size() != 0) {
            containerDataSource.removeAllItems();
        }
        containerDataSource.addAll(getCardOperations());
    }

    private List<CardOperation> getCardOperations() {
        List<CardOperation> cardOperations = Lists.newArrayList();
        if (holder != null) {
            cardOperations = getCardOperationDao().getByHolder(holder);
        }
        return cardOperations;
    }

    @Override
    public List<LayoutCommand> getCommands() {

        List<LayoutCommand> commands = Lists.newArrayList();

        LayoutCommand showFilter = new LayoutCommand(this);
        showFilter.setCaption("Фильтр");
        showFilter.setCommand(menuItem ->
                        CardOperationsFilterWindow.open(getCardOperationFilterParameters())
        );
        commands.add(showFilter);

        LayoutCommand clearFilter = new LayoutCommand(this);
        clearFilter.setCaption("Очистить фильтр");
        clearFilter.setCommand(menuItem -> {
            showError("Не реализовано");
        });
        commands.add(clearFilter);

        LayoutCommand showOperationData = new LayoutCommand(this);
        showOperationData.setCaption("Данные операции");
        showOperationData.setCommand(menuItem -> {
            CardOperation cardOperation = (CardOperation) getValue();
            CardOperationDetailViewWindow.open(cardOperation);
        });
        commands.add(showOperationData);

        return commands;
    }

    private void showCardOperationDetails(CardOperation cardOperation) {
        CardOperationDetailViewWindow.open(cardOperation);
    }

    private void showError(String text) {
        Notification.show(text, Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void cardholderFind(final DashboardEvent.CardholderFindEvent event) {
        setHolder(event.getHolder());
        updateDataContainer();
    }

    @Subscribe
    public void cardholderClear(final DashboardEvent.CardholderClearEvent event) {
        setHolder(null);
        updateDataContainer();
    }

    @Subscribe
    public void cardOperaionFilterChange(final DashboardEvent.CardOperaionFilterEvent event) {
        setCardOperationFilterParameters(event.getCardOperationFilterParameters());
        updateDataContainer();
    }
}

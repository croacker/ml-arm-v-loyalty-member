package ru.peaksystems.varm.loyalty.component;

import com.google.common.collect.Lists;
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
import ru.peak.ml.loyalty.core.data.dao.CardOperationDao;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.DashboardUI;
import ru.peaksystems.varm.loyalty.domain.MovieRevenue;
import ru.peaksystems.varm.loyalty.layout.LayoutCommand;
import ru.peaksystems.varm.loyalty.layout.MenuCommandsOwner;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("serial")
public final class CardOperationsTable extends Table implements MenuCommandsOwner {

    protected static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>()
    {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        }
    };

    private static DecimalFormat moneyFormat = new DecimalFormat("0.00");

    private Map<String, Object> filterParameters;

    private CardOperationDao cardOperationDao;

    /**
     * Список имен атрибутов (entityFieldName) класса CardOperation для отображения
     */
    private static Map<String, String> columnTitles = new LinkedHashMap<>();

    public Map<String, Object> getFilterParameters() {
        return filterParameters;
    }

    public void setFilterParameters(Map<String, Object> filterParameters) {
        this.filterParameters = filterParameters;
    }

    static {
        columnTitles.put("referenceNumber", "Номер ссылки");
        columnTitles.put("operationTime", "Дата");
        columnTitles.put("equipment", "Магазин");
        columnTitles.put("loyaltyType", "Тип");
        columnTitles.put("sum", "Сумма");
        columnTitles.put("sumLoyalty", "Вознаграждение");
    }

    public CardOperationDao getCardOperationDao() {
        if(cardOperationDao == null){
            cardOperationDao = GuiceConfigSingleton.inject(CardOperationDao.class);
        }
        return cardOperationDao;
    }

    @Override
    protected String formatPropertyValue(final Object rowId,
            final Object colId, final Property<?> property) {
        String result = super.formatPropertyValue(rowId, colId, property);
        if (colId.equals("sum") || colId.equals("sumLoyalty")) {
            if (property != null && property.getValue() != null) {
                Double r = Double.valueOf((Long)property.getValue());
                r = r/100;
                result = moneyFormat.format(r);
            } else {
                result = "0,00";
            }
        }else if(colId.equals("operationTime")){
            if(property != null && property.getValue() != null) {
                result = dateFormat.get().format(property.getValue());
            }else {
                result = StringUtil.EMPTY;
            }
        }else if(colId.equals("equipment")){
            if(property != null && property.getValue() != null) {
                Equipment equipment = (Equipment) property.getValue();
                if(equipment.getShop() != null){
                    result = equipment.getShop().getName();
                }else {
                    result = StringUtil.EMPTY;
                }
            }else {
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
//        setSortEnabled(false);
        setColumnAlignment("referenceNumber", Align.RIGHT);
        setRowHeaderMode(RowHeaderMode.INDEX);
//        setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        setSizeFull();

        List<MovieRevenue> movieRevenues = new ArrayList<>(
            DashboardUI.getDataProvider().getTotalMovieRevenues());
        Collections.sort(movieRevenues, (o1, o2) -> o2.getRevenue().compareTo(o1.getRevenue()));

        setContainerDataSource(new BeanItemContainer<>(
            CardOperation.class, getCardOperations()));

        setVisibleColumns(columnTitles.keySet().toArray());
//        setVisibleColumns(columnTitles.keySet());
        setColumnHeaders(columnTitles.values().toArray(new String[0]));
//        setColumnExpandRatio("title", 2);
//        setColumnExpandRatio("revenue", 1);

        setSortContainerPropertyId("referenceNumber");
        setSortAscending(false);

        addItemClickListener(itemClickEvent -> showCardOperationDetails((CardOperation) ((BeanItem) itemClickEvent.getItem()).getBean()));
    }

    public void updateDatasource(){
        BeanItemContainer dataContainer = (BeanItemContainer) getDataContainer();
        dataContainer.removeAllItems();
        dataContainer.addAll(getCardOperations());
    }

    private Container getEmptyDatasource(){
        return getDataContainer();
    }

    private Container getDataContainer() {
        return new BeanItemContainer<>(CardOperation.class);
    }

    private Collection getCardOperations(){
//        return getCardOperationDao().getByHolder((Holder) filterParameters.get("currentHolder"));
        return getCardOperationDao().getAll(CardOperation.class);
    }

    @Override
    public List<LayoutCommand> getCommands() {
        List<LayoutCommand> commands = Lists.newArrayList();

        LayoutCommand showFilter = new LayoutCommand(this);
        showFilter.setCaption("Фильтр");
        showFilter.setCommand(menuItem ->
                CardOperationsFilterWindow.open()
        );
        commands.add(showFilter);

        LayoutCommand clearFilter = new LayoutCommand(this);
        clearFilter.setCaption("Очистить фильтр");
        clearFilter.setCommand(menuItem -> showError("Не реализовано"));
        commands.add(clearFilter);

        return commands;
    }

    private void showCardOperationDetails(CardOperation cardOperation){
        CardOperationDetailViewWindow.open(cardOperation);
    }

    private void showError(String text){
        Notification.show(text, Notification.Type.ERROR_MESSAGE);
    }
}

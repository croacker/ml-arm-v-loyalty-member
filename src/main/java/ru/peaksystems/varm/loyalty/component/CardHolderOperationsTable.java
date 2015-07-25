package ru.peaksystems.varm.loyalty.component;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.CardOperation;
import ru.peak.ml.loyalty.core.data.dao.CardOperationDao;
import ru.peaksystems.varm.loyalty.DashboardUI;
import ru.peaksystems.varm.loyalty.domain.MovieRevenue;

import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("serial")
public final class CardHolderOperationsTable extends Table {

//    @Getter
//    @Setter
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
        columnTitles.put("equipment", "Терминал");//TODO: shop
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
        if (colId.equals("revenue")) {
            if (property != null && property.getValue() != null) {
                Double r = (Double) property.getValue();
                String ret = new DecimalFormat("#.##").format(r);
                result = "$" + ret;
            } else {
                result = "";
            }
        }
        return result;
    }

    public CardHolderOperationsTable() {
        setCaption("Операции");

        addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        addStyleName(ValoTheme.TABLE_SMALL);
        setSortEnabled(false);
        setColumnAlignment("revenue", Align.RIGHT);
        setRowHeaderMode(RowHeaderMode.INDEX);
        setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        setSizeFull();

        List<MovieRevenue> movieRevenues = new ArrayList<>(
            DashboardUI.getDataProvider().getTotalMovieRevenues());
        Collections.sort(movieRevenues, (o1, o2) -> o2.getRevenue().compareTo(o1.getRevenue()));

//        setContainerDataSource(new BeanItemContainer<>(
//            MovieRevenue.class, movieRevenues.subList(0, 10))
//        );
        setContainerDataSource(getEmptyDatasource());

        setVisibleColumns(columnTitles.keySet().toArray());
        setColumnHeaders(columnTitles.values().toArray(new String[0]));
//        setColumnHeaders("Title", "Revenue");
//        setColumnExpandRatio("title", 2);
//        setColumnExpandRatio("revenue", 1);

        setSortContainerPropertyId("operationTime");
        setSortAscending(false);
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

}

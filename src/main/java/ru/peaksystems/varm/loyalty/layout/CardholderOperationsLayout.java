package ru.peaksystems.varm.loyalty.layout;

import com.google.common.collect.Maps;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import ru.peak.ml.loyalty.core.data.MlUser;
import ru.peaksystems.varm.loyalty.component.CardHolderOperationsTable;

import java.util.Map;

/**
 *
 */
public class CardholderOperationsLayout  extends VerticalLayout {

    private MlUser userHolder;
    private CardHolderOperationsTable cardHolderOperationsTable;

    public CardholderOperationsLayout(){
        initComponents();
    }

    private void initComponents() {
        cardHolderOperationsTable = new CardHolderOperationsTable();//TODO добавить панель вокруг
        cardHolderOperationsTable.setWidth("100%");
        cardHolderOperationsTable.setPageLength(0);
        cardHolderOperationsTable.addStyleName("plain");
        cardHolderOperationsTable.addStyleName("borderless");
        cardHolderOperationsTable.setSortEnabled(false);
        cardHolderOperationsTable.setColumnAlignment("Revenue", Table.Align.RIGHT);
        cardHolderOperationsTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        addComponent(cardHolderOperationsTable);
    }

    public void setHolder(MlUser holder){
        this.userHolder = holder;
        aplyFilter();
    }

    public void aplyFilter(){
        Map<String, Object> filterParameters = Maps.newHashMap();
        filterParameters.put("currentHolder", this.userHolder.getHolder());
        cardHolderOperationsTable.setFilterParameters(filterParameters);
        cardHolderOperationsTable.updateDatasource();
    }

}

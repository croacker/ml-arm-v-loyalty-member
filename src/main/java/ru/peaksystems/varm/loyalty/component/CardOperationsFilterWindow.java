package ru.peaksystems.varm.loyalty.component;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Getter;
import lombok.Setter;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.Card;
import ru.peak.ml.loyalty.core.data.CardOperation;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peak.ml.loyalty.core.data.Shop;
import ru.peak.ml.loyalty.core.data.dao.CardOperationDao;
import ru.peak.ml.loyalty.core.data.mlenum.CardOperationType;
import ru.peaksystems.varm.loyalty.component.converter.CardConverter;
import ru.peaksystems.varm.loyalty.component.converter.OperationTypeConverter;
import ru.peaksystems.varm.loyalty.component.converter.ShopConverter;
import ru.peaksystems.varm.loyalty.domain.dto.CardOperationFilterParameters;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;

public class CardOperationsFilterWindow extends Window {

    public static final String ID = "transactionsfilterwindow";

    private Holder holder;

    private CardOperationDao cardOperationDao;

    @PropertyId("beginDate")
    private DateField beginDate;
    @PropertyId("endDate")
    private DateField endDate;
    @PropertyId("operationType")
    private ComboBox operationTypeCombobox;
    @PropertyId("shop")
    private ComboBox shopCombobox;
    @PropertyId("card")
    private ComboBox cardCombobox;

    private CardOperationFilterParameters cardOperationFilterParameters;

    private final BeanFieldGroup<CardOperationFilterParameters> fieldGroup;

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    public CardOperationDao getCardOperationDao() {
        if (cardOperationDao == null) {
            cardOperationDao = GuiceConfigSingleton.inject(CardOperationDao.class);
        }
        return cardOperationDao;
    }

    public CardOperationsFilterWindow(CardOperationFilterParameters cardOperationFilterParameters) {
        this.cardOperationFilterParameters = cardOperationFilterParameters;
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildFilterTab());
        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<>(CardOperationFilterParameters.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(cardOperationFilterParameters);
    }

    private Component buildFilterTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Фильтр операций");
        root.setIcon(FontAwesome.FILTER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        beginDate = new DateField("С");
        details.addComponent(beginDate);
        endDate = new DateField("По");
        details.addComponent(endDate);
        operationTypeCombobox = getOperationTypeCombobox();
        details.addComponent(operationTypeCombobox);
        shopCombobox = getShopCombobox();
        details.addComponent(shopCombobox);
        cardCombobox = getCardCombobox();
        details.addComponent(cardCombobox);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button apply = new Button("Применить");
        apply.addStyleName(ValoTheme.BUTTON_PRIMARY);
        apply.addClickListener(event -> {
            aplyFilter();
            close();
        });
        apply.focus();

        Button cancel = new Button("Отмена");
        cancel.addClickListener(event -> {
            close();
        });

        footer.addComponents(apply, cancel);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(apply, Alignment.TOP_RIGHT);

        return footer;
    }

    private ComboBox getOperationTypeCombobox() {
        ComboBox comboBox = new ComboBox("Тип операции");
        BeanItemContainer<CardOperationType> itemContainer = new BeanItemContainer<>(CardOperationType.class);
        itemContainer.addAll(Lists.newArrayList(CardOperationType.values()));
        comboBox.setImmediate(true);
        comboBox.setContainerDataSource(itemContainer);
        comboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        comboBox.setItemCaptionPropertyId("name");
        Converter converter = new OperationTypeConverter();
        comboBox.setConverter(converter);

        return comboBox;
    }

    private ComboBox getShopCombobox() {
        ComboBox comboBox = new ComboBox("ТСП");
        BeanItemContainer<Shop> itemContainer = new BeanItemContainer<>(Shop.class);
        itemContainer.addAll(cardOperationFilterParameters.getAvailableShops());
        comboBox.setImmediate(true);
        comboBox.setContainerDataSource(itemContainer);
        comboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        comboBox.setItemCaptionPropertyId("name");
        Converter converter = new ShopConverter(cardOperationFilterParameters.getAvailableShops());
        comboBox.setConverter(converter);
        return comboBox;
    }

    private ComboBox getCardCombobox() {
        ComboBox comboBox = new ComboBox("Карта");
        BeanItemContainer<Card> itemContainer = new BeanItemContainer<>(Card.class);
        itemContainer.addAll(cardOperationFilterParameters.getAvailableСards());
        comboBox.setImmediate(true);
        comboBox.setContainerDataSource(itemContainer);
        comboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        comboBox.setItemCaptionPropertyId("panHashNumber");
        Converter converter = new CardConverter(cardOperationFilterParameters.getAvailableСards());
        comboBox.setConverter(converter);
        return comboBox;
    }

    private void aplyFilter() {
        try {
            fieldGroup.commit();
            DashboardEventBus.post(new DashboardEvent.CardOperaionFilterEvent(this.cardOperationFilterParameters));
            close();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }

    }

    public static void open(CardOperationFilterParameters cardOperationFilterParameters) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new CardOperationsFilterWindow(cardOperationFilterParameters);
        UI.getCurrent().addWindow(w);
        w.focus();
    }

    private void updateComponents() {

    }

    @Subscribe
    public void cardholderFind(final DashboardEvent.CardholderFindEvent event) {
        setHolder(event.getHolder());
        updateComponents();
    }

    @Subscribe
    public void cardholderClear(final DashboardEvent.CardholderClearEvent event) {
        setHolder(null);
        updateComponents();
    }
}

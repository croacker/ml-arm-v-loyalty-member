package ru.peaksystems.varm.loyalty.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.peak.ml.loyalty.core.data.CardOperation;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;

public class CardOperationDetailViewWindow extends Window {

    public static final String ID = "operationdtailviewwindow";

    private final BeanFieldGroup<CardOperation> fieldGroup;
    private CardOperation cardOperation;

    @PropertyId("operationDate")
    private TextField operationDateField;
    @PropertyId("operationType")
    private TextField operationTypeField;
    @PropertyId("sum")
    private TextField sumField;
    @PropertyId("cashback")
    private TextField cashbackField;
    @PropertyId("organizationName")
    private TextField organizationNameField;
    @PropertyId("shopName")
    private TextField shopNameField;
    @PropertyId("legalAddress")
    private TextField legalAddressField;
    @PropertyId("partOfNumber")
    private TextField partOfNumberField;
    @PropertyId("socialCardType")
    private TextField socialCardTypeField;
    @PropertyId("paymentMethod")
    private TextField paymentMethodField;

    public CardOperationDetailViewWindow(final CardOperation cardOperation){
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

        detailsWrapper.addComponent(buildDetailTab());

        content.addComponent(buildFooter());
        fieldGroup = new BeanFieldGroup<>(CardOperation.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(cardOperation);
        this.cardOperation = cardOperation;
    }

    private Component buildDetailTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Личные данные");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        operationDateField = new TextField("Дата и время");
        operationDateField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(operationDateField);
        operationTypeField = new TextField("Тип");
        operationTypeField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(operationTypeField);
        sumField = new TextField("Сумма");
        sumField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(sumField);
        cashbackField = new TextField("Cashback");
        cashbackField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(cashbackField);
        organizationNameField = new TextField("Партнер");
        organizationNameField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(organizationNameField);
        shopNameField = new TextField("ТСП");
        shopNameField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(shopNameField);
        legalAddressField = new TextField("Адрес ТСП");
        legalAddressField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(legalAddressField);
        partOfNumberField = new TextField("Карта участника");
        partOfNumberField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(partOfNumberField);
        socialCardTypeField = new TextField("Тип социальной карты");
        socialCardTypeField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(socialCardTypeField);
        paymentMethodField = new TextField("Способ платежа");
        paymentMethodField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(paymentMethodField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("ОК");
        ok.addClickListener(event -> close());

        footer.addComponents(ok);
        footer.setExpandRatio(ok, 1);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

        return footer;
    }

    public static void open(final CardOperation cardOperation) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new CardOperationDetailViewWindow(cardOperation);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}

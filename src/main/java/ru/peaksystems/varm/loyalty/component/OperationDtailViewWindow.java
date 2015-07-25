package ru.peaksystems.varm.loyalty.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.peak.ml.loyalty.core.data.CardOperation;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;

public class OperationDtailViewWindow  extends Window {

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

    public OperationDtailViewWindow(final CardOperation cardOperation){
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

        detailsWrapper.addComponent(buildProfileTab());

        content.addComponent(buildFooter());
        fieldGroup = new BeanFieldGroup<>(CardOperation.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(cardOperation);
        this.cardOperation = cardOperation;
    }

    private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Личные данные");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        Image profilePic = new Image(null, new ThemeResource(
            "img/profile-pic-300px.jpg"));
        profilePic.setWidth(100.0f, Unit.PIXELS);
        pic.addComponent(profilePic);

        Button upload = new Button("Загрузить…", event -> {
            Notification.show("Не реализовано в этой версии");
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        root.addComponent(pic);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        operationDateField = new TextField("Дата и время");
        details.addComponent(operationDateField);
        operationTypeField = new TextField("Тип");
        details.addComponent(operationTypeField);
        sumField = new TextField("Сумма");
        details.addComponent(sumField);
        cashbackField = new TextField("Cashback");
        details.addComponent(cashbackField);
        organizationNameField = new TextField("Партнер");
        details.addComponent(organizationNameField);
        shopNameField = new TextField("ТСП");
        details.addComponent(shopNameField);
        legalAddressField = new TextField("Адрес ТСП");
        details.addComponent(legalAddressField);
        partOfNumberField = new TextField("Карта участника");
        details.addComponent(partOfNumberField);
        socialCardTypeField = new TextField("Тип социальной карты");
        details.addComponent(socialCardTypeField);
        paymentMethodField = new TextField("Способ платежа");
        details.addComponent(paymentMethodField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button cancel = new Button("Отмена");
        cancel.addClickListener(event -> close());

        footer.addComponents(cancel);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);

        return footer;
    }

    public static void open(final Holder holder) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new HolderProfilePreferencesWindow(holder);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}

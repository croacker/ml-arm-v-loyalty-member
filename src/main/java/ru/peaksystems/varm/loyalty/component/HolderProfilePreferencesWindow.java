package ru.peaksystems.varm.loyalty.component;


import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;
import ru.peaksystems.varm.loyalty.service.LoyaltyPersistServiceV;

public class HolderProfilePreferencesWindow extends Window {

    public static final String ID = "holderprofilepreferenceswindow";

    private LoyaltyPersistServiceV persistService;

    private final BeanFieldGroup<Holder> fieldGroup;
    private Holder holder;

    @PropertyId("socialCardNumber")
    private TextField socialCardNumberField;
    @PropertyId("lastName")
    private TextField lastNameField;
    @PropertyId("firstName")
    private TextField firstNameField;
    @PropertyId("secondName")
    private TextField secondNameField;
    @PropertyId("email")
    private TextField emailField;
    @PropertyId("phone")
    private TextField phoneField;
    @PropertyId("checkword")
    private TextField checkwordField;

    public LoyaltyPersistServiceV getPersistService() {
        if(persistService == null){
            persistService = GuiceConfigSingleton.inject(LoyaltyPersistServiceV.class);
        }
        return persistService;
    }

    public HolderProfilePreferencesWindow(final Holder holder){
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
        fieldGroup = new BeanFieldGroup<>(Holder.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(holder);
        this.holder = holder;
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

        socialCardNumberField = new TextField("Номер социальной карты");
        socialCardNumberField.setReadOnly(true);
        socialCardNumberField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(socialCardNumberField);
        lastNameField = new TextField("Фамилия");
        lastNameField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(lastNameField);
        firstNameField = new TextField("Имя");
        firstNameField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(firstNameField);
        secondNameField = new TextField("Отчество");
        secondNameField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(secondNameField);
        emailField = new TextField("Email");
        emailField.setReadOnly(true);
        emailField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(emailField);
        phoneField = new TextField("Телефон");
        phoneField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(emailField);
        checkwordField = new TextField("Проверочное слово");
        checkwordField.setNullRepresentation(StringUtil.EMPTY);
        details.addComponent(checkwordField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(event -> {
            try {
                fieldGroup.commit();
                getPersistService().merge(this.holder);

                Notification success = new Notification(
                    "Профиль держателя обновлен");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                DashboardEventBus.post(new DashboardEvent.CardholderUpdateEvent(this.holder));
                close();
            } catch (FieldGroup.CommitException e) {
                Notification.show("Ошибка обновления профиля держателя",
                    Notification.Type.ERROR_MESSAGE);
            }

        });
        ok.focus();

        Button cancel = new Button("Отмена");
        cancel.addClickListener(event -> close());

        footer.addComponents(ok, cancel);
        footer.setExpandRatio(cancel, 1);
//        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

        return footer;
    }

    public static void open(final Holder holder) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new HolderProfilePreferencesWindow(holder);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}

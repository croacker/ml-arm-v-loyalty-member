package ru.peaksystems.varm.loyalty.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.MlUser;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.CloseOpenWindowsEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.ProfileUpdatedEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;
import ru.peaksystems.varm.loyalty.service.LoyaltyPersistServiceV;

@SuppressWarnings("serial")
public class UserProfilePreferencesWindow extends Window {

    public static final String ID = "userprofilepreferenceswindow";

    private LoyaltyPersistServiceV persistService;

    private final BeanFieldGroup<MlUser> fieldGroup;
    private MlUser user;

    @PropertyId("firstName")
    private TextField firstNameField;
    @PropertyId("lastName")
    private TextField lastNameField;
//    @PropertyId("male")
    private OptionGroup sexField;
//    @PropertyId("email")
    private TextField emailField;
//    @PropertyId("phone")
    private TextField phoneField;
    @PropertyId("homePage")
    private TextField homePage;
    @PropertyId("login")
    private TextArea login;
    @PropertyId("password")
    private PasswordField password;

    public LoyaltyPersistServiceV getPersistService() {
        if(persistService == null){
            persistService = GuiceConfigSingleton.inject(LoyaltyPersistServiceV.class);
        }
        return persistService;
    }

    private UserProfilePreferencesWindow(final MlUser user, final boolean preferencesTabOpen) {
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
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
        detailsWrapper.addComponent(buildPreferencesTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(1);
        }

        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<>(MlUser.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
        this.user = user;
    }

    private Component buildPreferencesTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Параметры");
        root.setIcon(FontAwesome.COGS);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        Label message = new Label("Не реализовано в этой версии");
        message.setSizeUndefined();
        message.addStyleName(ValoTheme.LABEL_LIGHT);
        root.addComponent(message);
        root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

        return root;
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

        firstNameField = new TextField("Имя");
        details.addComponent(firstNameField);
        lastNameField = new TextField("Фамилия");
        details.addComponent(lastNameField);

        sexField = new OptionGroup("Пол");
        sexField.addItem(Boolean.TRUE);
        sexField.setItemCaption(Boolean.TRUE, "Мужской");
        sexField.addItem(Boolean.FALSE);
        sexField.setItemCaption(Boolean.FALSE, "Женский");
        sexField.addStyleName("horizontal");
        details.addComponent(sexField);

        Label section = new Label("Контакты");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setNullRepresentation("");
        details.addComponent(emailField);

        phoneField = new TextField("Телефон");
        phoneField.setWidth("100%");
        phoneField.setNullRepresentation("");
        details.addComponent(phoneField);

        section = new Label("Авторизация");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        homePage = new TextField("Домашняя страница");
        homePage.setWidth("100%");
        homePage.setNullRepresentation("");
        homePage.setReadOnly(true);
        details.addComponent(homePage);

        login = new TextArea("Логин");
        login.setWidth("100%");
        login.setRows(4);
        login.setNullRepresentation("");
        details.addComponent(login);

        password = new PasswordField ("Пароль");
        password.setWidth("100%");
        password.setNullRepresentation("");
        password.setValue(StringUtil.EMPTY);
        details.addComponent(password);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Сохранить");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(event -> {
            try {
                fieldGroup.commit();
                getPersistService().merge(this.user);

                Notification success = new Notification(
                    "Профиль пользователя обновлен");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                DashboardEventBus.post(new ProfileUpdatedEvent());
                close();
            } catch (CommitException e) {
                Notification.show("Ошибка обновления профиля пользователя",
                    Type.ERROR_MESSAGE);
            }

        });
        ok.setClickShortcut(KeyCode.ENTER, null);
        ok.focus();

        Button cancel = new Button("Отмена");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(KeyCode.ESCAPE, null);

        footer.addComponents(cancel, ok);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);

        return footer;
    }

    public static void open(final MlUser user, final boolean preferencesTabActive) {
        DashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new UserProfilePreferencesWindow(user, preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}

package ru.peaksystems.varm.loyalty.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.MlUser;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.UserLoginRequestedEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;
import ru.peaksystems.varm.loyalty.service.SecurityServiceV;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    private SecurityServiceV securityService;

    private SecurityServiceV getSecurityService() {
        if (securityService == null) {
            securityService = GuiceConfigSingleton.inject(SecurityServiceV.class);
        }
        return securityService;
    }

    public LoginView() {
        setSizeFull();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");

        Image msrPic = new Image(null, new ThemeResource("img/msc_logo.png"));
        msrPic.setWidth(200, Unit.PIXELS);
        headerLayout.addComponent(msrPic);
        headerLayout.setComponentAlignment(msrPic, Alignment.MIDDLE_LEFT);

        VerticalLayout contactsLayout = new VerticalLayout();
        Label phoneLabel = new Label("+7 (495) 539 55 55");
        contactsLayout.addComponent(phoneLabel);
        Label noteLabel = new Label("Горячая линия");
        contactsLayout.addComponent(noteLabel);
        contactsLayout.setSizeUndefined();

        headerLayout.addComponent(contactsLayout);
        headerLayout.setComponentAlignment(contactsLayout, Alignment.MIDDLE_RIGHT);

        addComponent(headerLayout);
        addComponent(buildSparklines());

        ///
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setExpandRatio(loginForm, 1);
        //

        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidth("100%");

        String footerContent = " <div id=\"template_footer_top\" class=\"template_area\"> "
        + " <div class=\"by_left\"> "
        + " <div id=\"copyright\">©&nbsp;2015&nbsp;ГУП&nbsp;МСР</div> "
                + " <address class=\"contacts\"> "
                + " <div class=\"address\">г. Москва, Серпуховской переулок, д. 7, стр. 1</div> "
                + " </address> "
                + " </div> "
                + " <div class=\"by_right\"> "
                + " <address class=\"contacts\"> "
                + " <div class=\"phone\">+7 (495) <strong>539 55 55</strong></div> "
                + " <div class=\"note\">Горячая линия</div> "
                + " </address> "
                + " <div id=\"worktime\">Время работы: 08:00 - 20:00 Пн-Вс</div> "
                + " </div> "
                + " </div> "
                + " <div id=\"template_footer_bottom\"> "
                + " <nav id=\"bottom_nav_menu\" class=\"template_area\"> "
                + " <ul class=\"nav_menu\"> "
                + " <li class=\"nav_menu_item\"><a class=\"nav_menu_item_link\" target=\"_blank\" href=\"http://www.soccard.ru/\">О "
                + " проекте</a></li> "
                + " <li class=\"nav_menu_item\"><a class=\"nav_menu_item_link\" target=\"_blank\" href=\"http://www.soccard.ru/\">О "
                + " предприятии</a></li> "
                + " <li class=\"nav_menu_item\"><a class=\"nav_menu_item_link\" target=\"_blank\" href=\"http://www.soccard.ru/\">Пресс-центр</a> "
                + " </li> "
                + " <li class=\"nav_menu_item\"><a class=\"nav_menu_item_link\" target=\"_blank\" href=\"http://www.soccard.ru/\">Стать "
                + " партнером</a></li> "
                + " </ul> "
                + " </nav> "
                + " </div> ";
        Label footerLabel = new Label(footerContent, ContentMode.HTML);
        footerLabel.setSizeFull();
        footerLayout.addComponent(footerLabel);

        addComponent(footerLayout);

        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(new CheckBox("Запомнить меня", true));
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Пользователь");
        username.setValue("admin");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Пароль");
        password.setValue("Admin896");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Авторизация");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(event -> sigIn(username.getValue(), password.getValue()));
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Добро пожаловать");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Call-центр Лояльность");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    private void sigIn(String userName, String password){
        if (StringUtil.isEmpty(userName)
            || StringUtil.isEmpty(userName)) {
            showError("Необходимо указать имя пользователя и пароль");
        } else {
            MlUser user = authenticateMember(userName, password);
            if (user != null) {
                DashboardEventBus.post(new UserLoginRequestedEvent(user));
            } else {
                showError("Неверное имя пользователя либо пароль.");
            }
        }
    }

    private MlUser authenticateMember(String login, String password) {
        return getSecurityService().authenticateMember(login, password);
    }

    private void showNotification(String text){
        Notification notification = new Notification(
            "Call-центр Лояльность");
        notification
            .setDescription(text);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(5000);
        notification.show(Page.getCurrent());
    }

    private void showError(String text){
        Notification.show(text, Notification.Type.ERROR_MESSAGE);
    }

    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);

        return sparks;
    }
}

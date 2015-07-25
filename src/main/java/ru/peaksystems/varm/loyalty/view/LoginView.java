package ru.peaksystems.varm.loyalty.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.core.model.security.MlUser;
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

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        showNotification("<span>Приложение - проект нового интерфейса для Оператора Call-центра.</span> <span>Ведите имя пользователя и пароль и нажмите <b>Авторизация</b> для того чтобы продолжить.</span>");
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
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Пароль");
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
                DashboardEventBus.post(new UserLoginRequestedEvent(userName, password));
            } else {
                showError("Неверное имя пользователя либо пароль. <span>Попробуйте admin, admin</span>");
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
        notification.setDelayMsec(10000);
        notification.show(Page.getCurrent());
    }

    private void showError(String text){
        Notification.show(text, Notification.Type.ERROR_MESSAGE);
    }
}

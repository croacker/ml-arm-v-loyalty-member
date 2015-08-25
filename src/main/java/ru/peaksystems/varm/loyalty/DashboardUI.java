package ru.peaksystems.varm.loyalty;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.MlUser;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.UserLoginRequestedEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.BrowserResizeEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.CloseOpenWindowsEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.UserLoggedOutEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;
import ru.peaksystems.varm.loyalty.service.SecurityServiceV;
import ru.peaksystems.varm.loyalty.view.LoginView;
import ru.peaksystems.varm.loyalty.view.MainView;

import java.util.Locale;

@Theme("dashboard")
@Widgetset("ru.peaksystems.varm.loyalty.DashboardWidgetSet")
@Title("ЛК сотрудника call-центра")
@SuppressWarnings("serial")
public final class DashboardUI extends UI {

    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();
    private static Locale RUSSIAN_LOCALE = new Locale("ru");

    private SecurityServiceV securityService;

    private SecurityServiceV getSecurityService() {
        if (securityService == null) {
            securityService = GuiceConfigSingleton.inject(SecurityServiceV.class);
        }
        return securityService;
    }

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(RUSSIAN_LOCALE);

        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(
            event -> DashboardEventBus.post(new BrowserResizeEvent()));
    }

    private void updateContent() {
        MlUser user = (MlUser) VaadinSession.getCurrent().getAttribute(
            MlUser.class.getName());
        if (user != null
//            && "admin".equals(user.getRole())
            ){
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
        MlUser user = event.getUser();
        VaadinSession.getCurrent().setAttribute(MlUser.class.getName(), user);
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    @Subscribe
    public void cardholderFind(final DashboardEvent.CardholderFindEvent event) {

    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((DashboardUI) getCurrent()).dashboardEventbus;
    }
}

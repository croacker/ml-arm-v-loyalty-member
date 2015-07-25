package ru.peaksystems.varm.loyalty;

import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import ru.peaksystems.varm.loyalty.event.DashboardEvent.BrowserResizeEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.CloseOpenWindowsEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.PostViewChangeEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;
import ru.peaksystems.varm.loyalty.view.DashboardViewType;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class DashboardNavigator extends Navigator {

    private static final String TRACKER_ID = null;
    private GoogleAnalyticsTracker tracker;

    private static final DashboardViewType ERROR_VIEW = DashboardViewType.DASHBOARD;
    private ViewProvider errorViewProvider;

    public DashboardNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);

        String host = getUI().getPage().getLocation().getHost();
        if (TRACKER_ID != null && host.endsWith("demo.vaadin.com")) {
            initGATracker(TRACKER_ID);
        }
        initViewChangeListener();
        initViewProviders();

    }

    private void initGATracker(final String trackerId) {
        tracker = new GoogleAnalyticsTracker(trackerId, "demo.vaadin.com");
        tracker.extend(UI.getCurrent());
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                DashboardViewType view = DashboardViewType.getByViewName(event
                        .getViewName());
                DashboardEventBus.post(new PostViewChangeEvent(view));
                DashboardEventBus.post(new BrowserResizeEvent());
                DashboardEventBus.post(new CloseOpenWindowsEvent());

                if (tracker != null) {
                    tracker.trackPageview("/dashboard/" + event.getViewName());
                }
            }
        });
    }

    private void initViewProviders() {
        for (final DashboardViewType viewType : DashboardViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(
                    viewType.getViewName(), viewType.getViewClass()) {

                private View cachedInstance;

                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType
                                        .getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            result = super.getView(viewType.getViewName());
                        }
                    }
                    return result;
                }
            };

            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}

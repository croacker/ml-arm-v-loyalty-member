package ru.peaksystems.varm.loyalty.view.dashboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import ru.peaksystems.varm.loyalty.component.CardOperationsTable;
import ru.peaksystems.varm.loyalty.event.DashboardEvent.CloseOpenWindowsEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;
import ru.peaksystems.varm.loyalty.layout.CardholderInfoLayout;
import ru.peaksystems.varm.loyalty.layout.CardholderSearchLayout;
import ru.peaksystems.varm.loyalty.layout.LayoutCommand;
import ru.peaksystems.varm.loyalty.layout.MenuCommandsOwner;
import ru.peaksystems.varm.loyalty.view.dashboard.DashboardEdit.DashboardEditListener;

import java.util.Iterator;

@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View,
        DashboardEditListener {

    private CssLayout dashboardPanels;
    private final VerticalLayout root;

    public DashboardView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        root.addLayoutClickListener(event -> DashboardEventBus.post(new CloseOpenWindowsEvent()));
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildSearchHolder());
        dashboardPanels.addComponent(buildCardholderInfo());
        dashboardPanels.addComponent(buildTop10TitlesByRevenue());

        return dashboardPanels;
    }

    private Component buildSearchHolder() {
        return createContentWrapper(new CardholderSearchLayout());
    }

    private Component buildCardholderInfo() {
        return createContentWrapper(new CardholderInfoLayout());
    }

    private Component buildTop10TitlesByRevenue() {
        Component contentWrapper = createContentWrapper(new CardOperationsTable());
        contentWrapper.addStyleName("top10-revenue");
        return contentWrapper;
    }

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        if(content instanceof Table){
            slot.addStyleName("dashboard-panel-slot-table");
        }else {
            slot.addStyleName("dashboard-panel-slot");
        }


        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuItem max = tools.addItem("", FontAwesome.EXPAND,
            selectedItem -> {
            if (!slot.getStyleName().contains("max")) {
                selectedItem.setIcon(FontAwesome.COMPRESS);
                toggleMaximized(slot, true);
            } else {
                slot.removeStyleName("max");
                selectedItem.setIcon(FontAwesome.EXPAND);
                toggleMaximized(slot, false);
            }
        });
        max.setStyleName("icon-only");
        MenuItem root = tools.addItem("", FontAwesome.COG, null);
        if(content instanceof MenuCommandsOwner){
            MenuCommandsOwner commandsOwner = (MenuCommandsOwner) content;
            for(LayoutCommand layoutCommand: commandsOwner.getCommands()){
                root.addItem(layoutCommand.getCaption(),
                    layoutCommand.getCommand());
            }
        }else {
            root.addItem("Редактировать", selectedItem -> Notification.show("Не реализовано в этой версии"));
            root.addSeparator();
            root.addItem("Закрыть", selectedItem -> Notification.show("Не реализовано в этой версии"));
        }

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    @Override
    public void enter(final ViewChangeEvent event) {

    }

    @Override
    public void dashboardNameEdited(final String name) {

    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }

}

package ru.peaksystems.varm.loyalty.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;

public class CardOperationsFilterWindow extends Window {

    public static final String ID = "transactionsfilterwindow";

    private DateField beginDate;
    private DateField endDate;
    private ComboBox operationTypeCombobox;
    private ComboBox shopCombobox;
    private ComboBox cardCombobox;

    public CardOperationsFilterWindow(){
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
        operationTypeCombobox = new ComboBox("Тип операции");
        details.addComponent(operationTypeCombobox);
        shopCombobox = new ComboBox("ТСП");
        details.addComponent(shopCombobox);
        cardCombobox = new ComboBox("Карта");
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
        cancel.addClickListener(event -> {close();});

        footer.addComponents(apply, cancel);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(apply, Alignment.TOP_RIGHT);

        return footer;
    }

    private void aplyFilter() {

    }

    public static void open() {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new CardOperationsFilterWindow();
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}

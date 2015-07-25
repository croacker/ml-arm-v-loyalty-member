package ru.peaksystems.varm.loyalty.component;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class TransactionsFilterWindow  extends Window {

    public static final String ID = "transactionsfilterwindow";


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

}

package ru.peaksystems.varm.loyalty.view.dashboard;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class DashboardEdit extends Window {

    private final TextField nameField = new TextField("Наименование");
    private final DashboardEditListener listener;

    public DashboardEdit(final DashboardEditListener listener,
            final String currentName) {
        this.listener = listener;
        setCaption("Редактировать");
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(300.0f, Unit.PIXELS);

        addStyleName("edit-dashboard");

        setContent(buildContent(currentName));
    }

    private Component buildContent(final String currentName) {
        VerticalLayout result = new VerticalLayout();
        result.setMargin(true);
        result.setSpacing(true);

        nameField.setValue(currentName);
        nameField.addStyleName("caption-on-left");
        nameField.focus();

        result.addComponent(nameField);
        result.addComponent(buildFooter());

        return result;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button cancel = new Button("Отмена");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(KeyCode.ESCAPE, null);

        Button save = new Button("Сохранить");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addClickListener(event -> {
            listener.dashboardNameEdited(nameField.getValue());
            close();
        });
        save.setClickShortcut(KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }

    public interface DashboardEditListener {
        void dashboardNameEdited(String name);
    }
}

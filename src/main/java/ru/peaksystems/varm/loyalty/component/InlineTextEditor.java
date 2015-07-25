package ru.peaksystems.varm.loyalty.component;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class InlineTextEditor extends CustomComponent {

    private final Property<String> property = new ObjectProperty<>(
        "Введите текст...");
    private final Component editor;
    private final Component readOnly;

    public InlineTextEditor(final String initialValue) {
        setWidth(100.0f, Unit.PERCENTAGE);
        addStyleName("inline-text-editor");

        editor = buildEditor();
        readOnly = buildReadOnly();

        if (initialValue != null) {
            property.setValue(initialValue);
        }

        setCompositionRoot(editor);
    }

    private Component buildReadOnly() {
        final Label text = new Label(property);
        text.setContentMode(ContentMode.HTML);

        Button editButton = new Button(FontAwesome.EDIT);
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editButton.addClickListener(event -> setCompositionRoot(editor));

        CssLayout result = new CssLayout(text, editButton);
        result.addStyleName("text-editor");
        result.setSizeFull();
        result.addLayoutClickListener(event -> {
            if (event.getChildComponent() == text && event.isDoubleClick()) {
                setCompositionRoot(editor);
            }
        });
        return result;
    }

    private Component buildEditor() {
        final RichTextArea rta = new RichTextArea(property);
        rta.setWidth(100.0f, Unit.PERCENTAGE);
        rta.addAttachListener(event -> {
            rta.focus();
            rta.selectAll();
        });

        Button save = new Button("Сохранить");
        save.setDescription("Редактировать");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addStyleName(ValoTheme.BUTTON_SMALL);
        save.addClickListener(event -> setCompositionRoot(readOnly));

        CssLayout result = new CssLayout(rta, save);
        result.addStyleName("edit");
        result.setSizeFull();
        return result;
    }

}

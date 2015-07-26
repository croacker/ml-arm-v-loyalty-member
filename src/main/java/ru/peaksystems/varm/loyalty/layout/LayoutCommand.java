package ru.peaksystems.varm.loyalty.layout;

import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;

/**
 *
 */
public class LayoutCommand {

    private Component owner;
    private String caption;
    private MenuBar.Command command;

    public LayoutCommand(Component owner){
        this.owner = owner;
    }

    public Component getOwner() {
        return owner;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public MenuBar.Command getCommand() {
        return command;
    }

    public void setCommand(MenuBar.Command command) {
        this.command = command;
    }

    public String getCaption() {
        return caption;
    }
}

package ru.peaksystems.varm.loyalty.layout;

import com.vaadin.ui.Component;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;

/**
 *
 */
public class CardholderClearCommand extends LayoutCommand{

    public CardholderClearCommand(Component owner) {
        super(owner);
        init();
    }

    private void init() {
        setCaption("Очистить");
        setCommand(menuItem -> {
            DashboardEventBus.post(new DashboardEvent.CardholderClearEvent());
        });
    }

}

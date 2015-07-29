package ru.peaksystems.varm.loyalty.layout;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.component.CardOperationDetailViewWindow;
import ru.peaksystems.varm.loyalty.component.HolderProfilePreferencesWindow;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;

import java.util.List;

public class CardholderInfoLayout extends VerticalLayout implements MenuCommandsOwner{

    private Holder holder;

    private Label socialCardNumber;
    private Label lastName;
    private Label firstName;
    private Label secondName;
    private Label email;
    private Label phone;
    private Label checkword;

    public CardholderInfoLayout(){
        initComponents();
        DashboardEventBus.register(this);
    }

    private void initComponents() {
        socialCardNumber = new Label("Номер социальной карты:");
        addComponent(socialCardNumber);
        lastName = new Label("Фамилия:");
        addComponent(lastName);
        firstName = new Label("Имя:");
        addComponent(firstName);
        secondName = new Label("Отчество:");
        addComponent(secondName);
        email = new Label("E-mail:");
        addComponent(email);
        phone = new Label("Номер телефона:");
        addComponent(phone);
        checkword = new Label("Проверочное слово:");
        addComponent(checkword);

    }

    private void updateComponents(){
        removeAllComponents();
        if(holder != null){
            socialCardNumber = new Label("Номер социальной карты:" + prepareValue(holder.getSocialCardNumber()));
            addComponent(socialCardNumber);
            lastName = new Label("Фамилия:" + prepareValue(holder.getLastName()));
            addComponent(lastName);
            firstName = new Label("Имя:" + prepareValue(holder.getFirstName()));
            addComponent(firstName);
            secondName = new Label("Отчество:" + prepareValue(holder.getSecondName()));
            addComponent(secondName);
            email = new Label("E-mail:" + prepareValue(holder.getEmail()));
            addComponent(email);
            phone = new Label("Номер телефона:" + prepareValue(holder.getPhone()));
            addComponent(phone);
            checkword = new Label("Проверочное слово:" + prepareValue(holder.getCheckword()));
            addComponent(checkword);
        }else {
            initComponents();
        }
    }

    private String prepareValue(String value){
        return StringUtil.isEmpty(value) ? StringUtil.EMPTY: value;
    }

    public void setHolder(Holder holder){
        this.holder = holder;
    }

    @Override
    public List<LayoutCommand> getCommands() {
        List<LayoutCommand> commands = Lists.newArrayList();
        commands.add(new CardholderClearCommand(this));

        LayoutCommand edit = new LayoutCommand(this);
        edit.setCaption("Редактировать");
        edit.setCommand(menuItem -> {
            if (holder != null) {
                HolderProfilePreferencesWindow.open(holder);
            }
        });
        commands.add(edit);

        return commands;
    }

    @Subscribe
    public void cardholderFind(final DashboardEvent.CardholderFindEvent event) {
        setHolder(event.getHolder());
        updateComponents();
    }

    @Subscribe
    public void cardholderClear(final DashboardEvent.CardholderClearEvent event) {
        setHolder(null);
        updateComponents();
    }

    @Subscribe
    public void cardholderUpdate(final DashboardEvent.CardholderUpdateEvent event) {
        setHolder(event.getHolder());
        updateComponents();
    }

    @Subscribe
    public void clickOperation(final DashboardEvent.ClickCardOperationEvent event) {
        if(event.getCardOperation() != null){
            CardOperationDetailViewWindow.open(event.getCardOperation());
        }
    }

    private void showError(String text){
        Notification.show(text, Notification.Type.ERROR_MESSAGE);
    }
}

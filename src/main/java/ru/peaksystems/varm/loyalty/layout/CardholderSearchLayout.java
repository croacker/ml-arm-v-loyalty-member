package ru.peaksystems.varm.loyalty.layout;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.*;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peak.ml.loyalty.core.data.dao.HolderDao;
import ru.peak.ml.loyalty.util.StringUtil;
import ru.peaksystems.varm.loyalty.event.DashboardEvent;
import ru.peaksystems.varm.loyalty.event.DashboardEventBus;

import java.util.List;

public class CardholderSearchLayout extends VerticalLayout implements MenuCommandsOwner{

    private HolderDao holderDao;

    TextField cardNuberField;
    TextField checkwordField;

    public HolderDao getHolderDao() {
        if(holderDao == null){
            holderDao = GuiceConfigSingleton.inject(HolderDao.class);
        }
        return holderDao;
    }

    public CardholderSearchLayout(){
        initComponents();
        DashboardEventBus.register(this);
    }

    private void initComponents() {
        setSpacing(true);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setWidth("100%");
        cardNuberField = new TextField();
        cardNuberField.setWidth("100%");
        cardNuberField.setInputPrompt("Ноомер социальной карты, либо e-mail");
        layout.addComponent(cardNuberField);
        layout.setComponentAlignment(cardNuberField, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(cardNuberField, 1.0f);
        addComponent(layout);

        layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setWidth("100%");
        checkwordField = new TextField();
        checkwordField.setWidth("100%");
        checkwordField.setInputPrompt("Проверочное слово");
        layout.addComponent(checkwordField);
        layout.setComponentAlignment(checkwordField, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(checkwordField, 1.0f);
        addComponent(layout);

        Button btnSearch = new Button("Найти");
        btnSearch.addClickListener(getSearchClickListener());
        addComponent(btnSearch);
    }

    private Button.ClickListener getSearchClickListener(){
        return clickEvent -> search();
    }

    private void search(){
        if(StringUtil.isEmpty(cardNuberField.getValue()) || StringUtil.isEmpty(checkwordField.getValue())){
            showError("Необходимо указать Логин и Проверочное слово");
            return;
        }
        Holder holder =  getHolderDao().getBySocialCardNumberAndCheckword(cardNuberField.getValue().trim(), checkwordField.getValue().trim());
        if(holder == null){
            showError("Держатель с указанными данными не обнаружен");
            return;
        }
        DashboardEventBus.post(new DashboardEvent.CardholderFindEvent(holder));
    }

    @Override
    public List<LayoutCommand> getCommands() {
        List<LayoutCommand> commands = Lists.newArrayList();
        commands.add(new CardholderClearCommand(this));
        return commands;
    }

    @Subscribe
    public void cardholderClear(final DashboardEvent.CardholderClearEvent event) {
        cardNuberField.clear();
        checkwordField.clear();
    }

    private void showError(String text){
        Notification.show(text, Notification.Type.ERROR_MESSAGE);
    }

}

package ru.peaksystems.varm.loyalty.layout;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.peak.ml.loyalty.core.data.MlUser;

public class CardholderInfoLayout extends VerticalLayout{

    private MlUser userHolder;

    private Label socialCardNumber;
    private Label lastName;
    private Label firstName;
    private Label secondName;
    private Label email;
    private Label phone;
    private Label checkword;

    public CardholderInfoLayout(){
        initComponents();
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
        if(userHolder != null){
            socialCardNumber = new Label("Номер социальной карты:" + userHolder.getHolder().getSocialCardNumber());
            addComponent(socialCardNumber);
            lastName = new Label("Фамилия:" + userHolder.getHolder().getLastName());
            addComponent(lastName);
            firstName = new Label("Имя:" + userHolder.getHolder().getFirstName());
            addComponent(firstName);
            secondName = new Label("Отчество:" + userHolder.getHolder().getSecondName());
            addComponent(secondName);
            email = new Label("E-mail:" + userHolder.getHolder().getEmail());
            addComponent(email);
            phone = new Label("Номер телефона:" + userHolder.getHolder().getPhone());
            addComponent(phone);
            checkword = new Label("Проверочное слово:" + userHolder.getHolder().getCheckword());
            addComponent(checkword);
        }else {
            initComponents();
        }
    }

    public void setHolder(MlUser userHolder){
        this.userHolder = userHolder;
        updateComponents();
    }

}

package ru.peaksystems.varm.loyalty.layout;

import com.vaadin.ui.*;

public class CardholderSearchLayout extends VerticalLayout{

    public CardholderSearchLayout(){
        initComponents();
    }

    private void initComponents() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        TextField loginField = new TextField();
        loginField.setWidth("100%");
        loginField.setInputPrompt("Ноомер социальной карты, либо e-mail");
        layout.addComponent(loginField);
        layout.setComponentAlignment(loginField, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(loginField, 1.0f);
        addComponent(layout);

        layout = new HorizontalLayout();
        layout.setWidth("100%");
        TextField checkwordField = new TextField();
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
//        callcenterOperatorDesktop.searchCardholder(getLogin(), getCheckword());
    }
}

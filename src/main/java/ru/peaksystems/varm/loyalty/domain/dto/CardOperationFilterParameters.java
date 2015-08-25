package ru.peaksystems.varm.loyalty.domain.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import ru.peak.ml.loyalty.core.data.Card;
import ru.peak.ml.loyalty.core.data.Holder;
import ru.peak.ml.loyalty.core.data.Shop;
import ru.peak.ml.loyalty.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 *
 */
public class CardOperationFilterParameters {

    private Holder holder;

    //Значения фильтра
    private Date beginDate;
    private Date endDate;
    private String operationType;
    private Shop shop;
    private Card card;

    /**
     * Магазины которые могут применяться при фильтрации операций
     */
    private List<Shop> availableShops = Lists.newArrayList();

    /**
     * Карты которые могут применяться при фильтрации операций
     */
    private List<Card> availableСards = Lists.newArrayList();

    public void clear() {
        beginDate = null;
        endDate = null;
        operationType = StringUtil.EMPTY;
        shop = null;
        card = null;
    }

    public List<Shop> getAvailableShops() {
        return availableShops;
    }

    public void setAvailableShops(List<Shop> availableShops) {
        this.availableShops = availableShops;
    }

    public List<Card> getAvailableСards() {
        return availableСards;
    }

    public void setAvailableСards(List<Card> availableСards) {
        this.availableСards = availableСards;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }
}

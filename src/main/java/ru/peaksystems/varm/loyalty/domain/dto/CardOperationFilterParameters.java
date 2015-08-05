package ru.peaksystems.varm.loyalty.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 */
public class CardOperationFilterParameters {

    @Getter @Setter
    public Date beginDate;
    @Getter @Setter
    public Date endDate;
    @Getter @Setter
    public String operationType;
    @Getter @Setter
    public String shop;
    @Getter @Setter
    public String card;
}

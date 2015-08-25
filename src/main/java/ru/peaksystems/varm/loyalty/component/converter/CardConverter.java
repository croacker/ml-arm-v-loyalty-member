package ru.peaksystems.varm.loyalty.component.converter;

import com.vaadin.data.util.converter.Converter;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.Card;
import ru.peak.ml.loyalty.core.data.dao.CardDao;
import ru.peak.ml.loyalty.util.StringUtil;

import java.util.List;
import java.util.Locale;

/**
 *
 */
public class CardConverter implements Converter<Card, String> {

    List<Card> cards;

    public CardConverter(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String convertToModel(Card value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if(value == null){
            return StringUtil.EMPTY;
        }else {
            return value.getPanHashNumber();
        }
    }

    @Override
    public Card convertToPresentation(String value, Class<? extends Card> targetType, Locale locale) throws ConversionException {
        return cards.stream().filter(card -> card.getPanHashNumber().equals(value)).findFirst().orElse(null);
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<Card> getPresentationType() {
        return Card.class;
    }

}

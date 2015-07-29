package ru.peaksystems.varm.loyalty.component.converter;

import com.vaadin.data.util.converter.Converter;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.Card;
import ru.peak.ml.loyalty.core.data.dao.CardDao;

import java.util.Locale;

/**
 *
 */
public class CardConverter implements Converter<Card, String> {

    CardDao cardDao;

    private CardDao getCardDao(){
        if(cardDao == null){
            cardDao = GuiceConfigSingleton.inject(CardDao.class);
        }
        return cardDao;
    }

    @Override
    public String convertToModel(Card value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value.getPanHashNumber();
    }

    @Override
    public Card convertToPresentation(String value, Class<? extends Card> targetType, Locale locale) throws ConversionException {
        return getCardDao().getByPanHashNumber(value);
    }

    @Override
    public Class<String> getModelType() {
        return null;
    }

    @Override
    public Class<Card> getPresentationType() {
        return null;
    }

}

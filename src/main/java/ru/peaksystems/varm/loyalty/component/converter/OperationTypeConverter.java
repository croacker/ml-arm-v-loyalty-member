package ru.peaksystems.varm.loyalty.component.converter;

import com.vaadin.data.util.converter.Converter;
import ru.peak.ml.loyalty.core.data.mlenum.CardOperationType;
import ru.peak.ml.loyalty.util.StringUtil;

import java.util.Locale;

/**
 *
 */
public class OperationTypeConverter implements Converter<CardOperationType, String> {

    @Override
    public String convertToModel(CardOperationType value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if(value != null) {
            return value.getCode();
        }else{
            return StringUtil.EMPTY;
        }
    }

    @Override
    public CardOperationType convertToPresentation(String value, Class<? extends CardOperationType> targetType, Locale locale) throws ConversionException {
        return CardOperationType.lookup(value);
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<CardOperationType> getPresentationType() {
        return CardOperationType.class;
    }
}

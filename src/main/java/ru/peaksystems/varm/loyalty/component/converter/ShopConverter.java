package ru.peaksystems.varm.loyalty.component.converter;

import com.vaadin.data.util.converter.Converter;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.Shop;
import ru.peak.ml.loyalty.core.data.dao.ShopDao;
import ru.peak.ml.loyalty.util.StringUtil;

import java.util.List;
import java.util.Locale;

/**
 *
 */
public class ShopConverter implements Converter<Shop, String> {

    List<Shop> shops;

    public ShopConverter(List<Shop> shops) {
        this.shops = shops;
    }

    @Override
    public String convertToModel(Shop value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if(value == null){
            return StringUtil.EMPTY;
        }else{
            return value.getName();
        }
    }

    @Override
    public Shop convertToPresentation(String value, Class<? extends Shop> targetType, Locale locale) throws ConversionException {
        return shops.stream().filter(shop -> shop.getName().equals(value)).findFirst().orElse(null);
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<Shop> getPresentationType() {
        return Shop.class;
    }
}

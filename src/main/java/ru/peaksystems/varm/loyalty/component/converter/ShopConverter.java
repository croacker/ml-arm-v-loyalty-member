package ru.peaksystems.varm.loyalty.component.converter;

import com.vaadin.data.util.converter.Converter;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.loyalty.core.data.Shop;
import ru.peak.ml.loyalty.core.data.dao.ShopDao;

import java.util.Locale;

/**
 *
 */
public class ShopConverter implements Converter<Shop, String> {

    ShopDao shopDao;

    private ShopDao getShopDao(){
        if(shopDao == null){
            shopDao = GuiceConfigSingleton.inject(ShopDao.class);
        }
        return shopDao;
    }

    @Override
    public String convertToModel(Shop value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value.getName();
    }

    @Override
    public Shop convertToPresentation(String value, Class<? extends Shop> targetType, Locale locale) throws ConversionException {
        return getShopDao().finByName(value);
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

package ru.peak.ml.web.settings;

import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.core.dao.CommonDao;
import ru.peak.ml.prop.PropertyProvider;
import ru.peak.ml.web.model.MlSettings;

import java.util.List;
import java.util.Properties;

/**
 * Created by user on 06.06.2015.
 */
public class DBPropertyProvider implements PropertyProvider {
  @Override
  public Properties getProperties() {
    Properties properties = new Properties();
    CommonDao commonDao = GuiceConfigSingleton.inject(CommonDao.class);
    List<MlSettings> settings = commonDao.getResultList("select o from MlSettings o",MlSettings.class);
    for(MlSettings setting : settings){
      properties.put(setting.getName(),setting.getValue());
    }
    return properties;
  }
}

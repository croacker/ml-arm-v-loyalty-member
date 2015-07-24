package ru.peak.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import ru.peak.ml.core.initializer.MlMetaDataInitializeService;
import ru.peak.ml.core.initializer.impl.MetaDataInitializeServiceImpl;
import ru.peak.ml.core.initializer.jpa.JPADynamicClassInitializerService;
import ru.peak.ml.core.initializer.jpa.impl.JPADynamicClassInitializerImpl;
import ru.peak.ml.core.services.AccessService;
import ru.peak.ml.prop.MlProperties;
import ru.peak.ml.prop.PropertyProvider;
import ru.peak.ml.prop.impl.CMSPropertiesFileProvider;
import ru.peak.ml.prop.impl.MlPropertiesImpl;
import ru.peak.ml.prop.impl.ProjectPropertiesFileProvider;
import ru.peak.ml.web.settings.DBPropertyProvider;
import ru.peak.security.services.AccessServiceImpl;

/**
 * Created by user on 06.06.2015.
 */
public class BindModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.bind(MlMetaDataInitializeService.class).to(MetaDataInitializeServiceImpl.class);
    binder.bind(JPADynamicClassInitializerService.class).to(JPADynamicClassInitializerImpl.class);
    binder.bind(MlProperties.class).to(MlPropertiesImpl.class);
//    binder.bind(IGuiceModules.class).to(GuiceModules.class);
    binder.bind(AccessService.class).to(AccessServiceImpl.class);

    // для получения коллекции элементов через инжектор GuiceConfigSingleton.inject(Key.get(Multibinder.setOf(new TypeLiteral(PropertyProvider.class))))
    Multibinder<PropertyProvider> multibinder = Multibinder.newSetBinder(binder,PropertyProvider.class);
    multibinder.addBinding().to(CMSPropertiesFileProvider.class);
    multibinder.addBinding().to(ProjectPropertiesFileProvider.class);
    multibinder.addBinding().to(DBPropertyProvider.class);
  }
}

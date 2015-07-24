package ru.peak.modules;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by user on 06.06.2015.
 */
public class JpaModule extends ServletModule {
  private static final Logger log = LoggerFactory.getLogger(JpaModule.class);

  public void configureServlets() {
    log.debug("Install JpaPersist");
    JpaPersistModule mainJpaModule = new JpaPersistModule("MainMlCmsUnit");
    Properties dbProps = new Properties();
    DynamicClassLoader dcl = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
    dbProps.put(PersistenceUnitProperties.CLASSLOADER, dcl);
    dbProps.put(PersistenceUnitProperties.WEAVING, "static");
    mainJpaModule.properties(dbProps);
    install(mainJpaModule);
  }
}
package ru.peaksystems.varm.servlet;

import com.google.inject.persist.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.core.holders.SecurityHolder;
import ru.peak.ml.core.initializer.MlMetaDataInitializeService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 */
@Slf4j
public class MlBootstrapListenerV implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    UnitOfWork unitOfWork = GuiceConfigSingleton.inject(UnitOfWork.class);
//    log.debug("Try Start UnitOfWork");
    unitOfWork.begin();
    try {
//      log.debug("Start MlBootstrapListener, Try to initialize MetaData");
      MlMetaDataInitializeService mlMetaDataInitializeService = GuiceConfigSingleton.inject(MlMetaDataInitializeService.class);
      mlMetaDataInitializeService.initializeAllMetaData();
//      log.debug("Start MlBootstrapListener started. Meta Data ready to work.");
    } finally {
//      log.debug("Try Stop UnitOfWork");
      unitOfWork.end();
    }
    GuiceConfigSingleton.inject(SecurityHolder.class).initAllRoles();
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
//    log.debug("Destroy context of MlBootStrapListener");
  }
}

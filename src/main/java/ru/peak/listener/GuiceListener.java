package ru.peak.listener;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;
import lombok.extern.slf4j.Slf4j;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.modules.GuiceModules;

import javax.servlet.ServletContextEvent;
import java.util.List;

/**
 *
 */
@Slf4j
public class GuiceListener extends GuiceServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
//    log.debug("Start ObjectModelGuiceInitContextListener contextInitialized");
    super.contextInitialized(servletContextEvent);
    // Стартуем перситенс сервис тут т.к. он нам понадобится при инициализации мета данных
//    log.debug("Start PersistService");
    GuiceConfigSingleton.inject(PersistService.class).start();
//    log.info("Guice Context Listener started");

  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    //persistService.stop();
    super.contextDestroyed(servletContextEvent);
  }

  @Override
  protected Injector getInjector() {
    List<Module> modules = (new GuiceModules()).getGuiceModules();
    GuiceConfigSingleton gcs = GuiceConfigSingleton.getInstance(modules);
    return gcs.getInjector();
  }
}

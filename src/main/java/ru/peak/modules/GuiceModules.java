package ru.peak.modules;

import com.google.inject.Module;
import ru.ml.core.common.guice.IGuiceModules;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GuiceModules implements IGuiceModules {
  @Override
  public List<Module> getGuiceModules() {
    List<Module> modules = new ArrayList<>();
    modules.add(new BindModule());
    modules.add(new JpaModule());
    return modules;
  }
}

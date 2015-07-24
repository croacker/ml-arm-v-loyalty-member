package ru.peak.ml.web.model;

import org.eclipse.persistence.internal.dynamic.DynamicPropertiesManager;
import ru.peak.ml.core.model.MlDynamicEntityImpl;

/**
 *
 */
public class MlSettings extends MlDynamicEntityImpl {
  public static DynamicPropertiesManager DPM = new DynamicPropertiesManager();

  @Override
  public DynamicPropertiesManager fetchPropertiesManager() {
    return DPM;  //To change body of implemented methods use File | Settings | File Templates.
  }

  protected static class Properties{
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String DESCRIPTION = "description";
  }

  public String getName(){
    return get(Properties.NAME);
  }

  public String getValue() {
    return get(Properties.VALUE);
  }

  public String getDescription() {
    return get(Properties.DESCRIPTION);
  }

  @Override
  public String toString() {
    return "("+getDescription()+")"+getName()+"="+getValue();
  }
}

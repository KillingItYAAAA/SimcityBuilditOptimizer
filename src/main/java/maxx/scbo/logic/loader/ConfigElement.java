package maxx.scbo.logic.loader;

import maxx.scbo.logic.config.Configuration;

import org.xml.sax.Attributes;

public abstract class ConfigElement {
  private ConfigLoader configLoader;
  
  ConfigElement(ConfigLoader configLoader) {
    this.configLoader = configLoader;
  }
  
  protected ConfigLoader getConfigLoader() {
    return this.configLoader;
  }
  
  protected Configuration getConfiguration() {
    return getConfigLoader().getConfiguration();
  }
  
  public abstract void start(Attributes attributes);

  public abstract void end();
}
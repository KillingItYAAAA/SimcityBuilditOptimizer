package maxx.scbo.logic.loader;

import maxx.scbo.logic.config.ConfigStore;
import maxx.scbo.logic.config.Configuration;

import org.xml.sax.Attributes;

class ConfigElementProducer extends ConfigElement {
  /**
   * 
   */
  private final ConfigLoader configLoader;

  public ConfigElementProducer(ConfigLoader configLoader, Configuration configuration) {
    super(configuration);
    this.configLoader = configLoader;
  }

  public void start(Attributes attributes) {
    new ConfigStore(this.configLoader.configuration, attributes.getValue("name"));
  }

  public void end() {
    this.configLoader.configProducer = null;
  }
}
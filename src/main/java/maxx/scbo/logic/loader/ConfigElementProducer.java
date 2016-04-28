package maxx.scbo.logic.loader;

import maxx.scbo.logic.config.ConfigStore;

import org.xml.sax.Attributes;

class ConfigElementProducer extends ConfigElement {
  public ConfigElementProducer(ConfigLoader configLoader) {
    super(configLoader);
  }

  public void start(Attributes attributes) {
    new ConfigStore(getConfiguration(), attributes.getValue("name"));
  }

  public void end() {
    getConfigLoader().setConfigProducer(null);
  }
}
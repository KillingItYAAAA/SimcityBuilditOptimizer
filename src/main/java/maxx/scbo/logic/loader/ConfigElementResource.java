package maxx.scbo.logic.loader;

import maxx.scbo.logic.config.ConfigResource;
import maxx.scbo.logic.config.Configuration;

import org.xml.sax.Attributes;

class ConfigElementResource extends ConfigElement {
  /**
   * 
   */
  private final ConfigLoader configLoader;

  public ConfigElementResource(ConfigLoader configLoader, Configuration configuration) {
    super(configuration);
    this.configLoader = configLoader;
  }

  public void start(Attributes attributes) {
    assert this.configLoader.configResource == null;
    assert this.configLoader.configProducer != null;

    String name = attributes.getValue("name");
    Integer level = Integer.parseInt(attributes.getValue("level"));
    Double time = Double.parseDouble(attributes.getValue("time"));
    Double value = Double.parseDouble(attributes.getValue("value"));

    this.configLoader.configResource = new ConfigResource(this.configLoader.configProducer, name, time, level, value);
  }

  public void end() {
    this.configLoader.configResource = null;
  }
}
package maxx.scbo.logic.loader;

import maxx.scbo.logic.config.ConfigResource;
import maxx.scbo.logic.config.Configuration;

import org.xml.sax.Attributes;

class ConfigElementResource extends ConfigElement {
  public ConfigElementResource(ConfigLoader configLoader) {
    super(configLoader);
  }

  public void start(Attributes attributes) {
    assert getConfigLoader().getConfigResource() == null;
    assert getConfigLoader().getConfigProducer() != null;

    String name = attributes.getValue("name");
    Integer level = Integer.parseInt(attributes.getValue("level"));
    Double time = Double.parseDouble(attributes.getValue("time"));
    Double value = Double.parseDouble(attributes.getValue("value"));

    new ConfigResource(getConfigLoader().getConfigProducer(), name, time, level, value);
  }

  public void end() {
    getConfigLoader().setConfigResource(null);
  }
}
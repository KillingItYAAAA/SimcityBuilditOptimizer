package maxx.scbo.logic.loader;

import maxx.scbo.logic.config.Configuration;
import maxx.scbo.logic.config.RawRelation;

import org.xml.sax.Attributes;

class ConfigElementRaw extends ConfigElement {
  /**
   * 
   */
  private final ConfigLoader configLoader;

  public ConfigElementRaw(ConfigLoader configLoader, Configuration configuration) {
    super(configuration);
    this.configLoader = configLoader;
  }

  public void start(Attributes attributes) {
    assert this.configLoader.configResource != null;

    String name = attributes.getValue("resource");
    int rawNo = Integer.parseInt(attributes.getValue("number"));

    RawRelation rawRelation = new RawRelation(this.configLoader.configResource.getName(), name, rawNo);
    this.configLoader.rawRelations.add(rawRelation);
  }

  public void end() {

  }
}
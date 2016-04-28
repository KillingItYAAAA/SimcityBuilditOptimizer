package maxx.scbo.logic.loader;

import org.xml.sax.Attributes;

class ConfigElementRaw extends ConfigElement {
  public ConfigElementRaw(ConfigLoader configLoader) {
    super(configLoader);
  }

  public void start(Attributes attributes) {
    assert getConfigLoader().getConfigResource() != null;

    String name = attributes.getValue("resource");
    int rawNo = Integer.parseInt(attributes.getValue("number"));

    RawRelation rawRelation = new RawRelation(getConfigLoader().getConfigResource().getName(),
        name, rawNo);
    this.configLoader.rawRelations.add(rawRelation);
  }

  public void end() {

  }
}
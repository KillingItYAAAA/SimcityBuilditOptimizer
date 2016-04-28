package maxx.scbo.logic.loader;

import maxx.scbo.logic.config.Configuration;

import org.xml.sax.Attributes;

abstract class ConfigElement {
  private Configuration configuration;

  public ConfigElement(Configuration configuration) {
    this.configuration = configuration;
  }

  public abstract void start(Attributes attributes);

  public abstract void end();
}
package maxx.scbo.logic.config;

import maxx.scbo.helper.Checkable;
import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.scenario.Resource;

import java.util.LinkedList;
import java.util.TreeMap;

public abstract class ConfigProducer implements Checkable {
  private Configuration configuration;
  
  private TreeMap<Integer, ConfigResource> configResources = new TreeMap<Integer, ConfigResource>();

  public ConfigProducer(Configuration configuration) throws ScboException {
    this.configuration = configuration;
    configuration.addConfigProducer(this);
  }
  
  public Configuration getConfiguration() {
    return this.configuration;
  }
  
  @Override
  public void checkValid() {
    assert getConfiguration().hasConfigProducer(this);
  }
  
  public void addConfigResource(ConfigResource configResource) {
    configResources.put(configResource.getConfigurationIdx(), configResource);
  }
  
  public LinkedList<Resource> getResources() {
    return resources;
  }
}
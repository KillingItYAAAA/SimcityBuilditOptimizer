package maxx.scbo.logic.config;

import maxx.scbo.helper.Id;
import maxx.scbo.logic.scenario.Resource;

import java.util.TreeMap;
import java.util.TreeSet;

public class ConfigResource {
  private ConfigProducer configProducer;
  private String name;
  private double prodTime;
  private int minLevel;
  private double value;
  private TreeSet<ConfigResource> rawsFor = new TreeSet<ConfigResource>();
  private TreeMap<Resource, Integer> rawMaterials = new TreeMap<Resource, Integer>();
  private Id configurationIdx;

  public Configuration getConfiguration() {
    return configProducer.getConfiguration();
  }

  public ConfigResource(ConfigProducer configProducer, String name, double prodTime, int minLevel,
      double value) {
    this.configProducer = configProducer;
    
    assert name != null;
    this.name = name;
    
    assert prodTime > 0;
    this.prodTime = prodTime;
    
    assert minLevel > 0;
    this.minLevel = minLevel;
    
    assert value > 0;
    this.value = value;
    
    configurationIdx = getConfiguration().getResourceIdFactory().get();
    configProducer.addConfigResource(this);
  }

  public String getName() {
    return name;
  }
  
  public int getConfigurationIdx() {
    return configurationIdx.getId();
  }
  
  public double getProdTime() {
    return this.prodTime;
  }
}
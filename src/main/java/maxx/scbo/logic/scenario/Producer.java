package maxx.scbo.logic.scenario;

import maxx.scbo.logic.config.ConfigProducer;
import maxx.scbo.logic.config.ConfigResource;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;
import java.util.TreeMap;

public abstract class Producer extends ConstraintSource {
  private ConfigProducer configProducer;
  private Scenario scenario;
  private TreeMap<ConfigResource, Resource> resources = new TreeMap<ConfigResource, Resource>();
  
  public Producer(ConfigProducer configProducer, Scenario scenario) {
    super(scenario);
    this.configProducer = configProducer;
    this.scenario = scenario;
    scenario.addProducer(this);
  }

  public ConfigProducer getConfigProducer() {
    return configProducer;
  }
  
  public void addResource(Resource resource) {
    assert resources.containsKey(resource.getConfigResource());
    resources.put(resource.getConfigResource(), resource);
  }
  
  
  @Override
  public LinkedList<LinearConstraint> getConstraints() {
    // TODO Auto-generated method stub
    return null;
  }

  public abstract double getProdAccel();
}
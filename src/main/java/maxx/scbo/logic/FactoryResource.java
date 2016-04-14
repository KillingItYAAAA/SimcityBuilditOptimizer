package maxx.scbo.logic;

import maxx.scbo.ScboException;
import maxx.scbo.Scenario;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public class FactoryResource extends Resource {
  public ResourceType getType() {
    return ResourceType.FACTORY;
  }
  
  FactoryResource(Scenario scenario) throws ScboException {
    super(scenario);
    scenario.getFactory().addResource(this);
  }
  
  @Override
  public void checkValid() throws ScboException {
    super.checkValid();
  }
  
  @Override
  public LinkedList<LinearConstraint> getConstraints() {
    return super.getConstraints();
  }
}
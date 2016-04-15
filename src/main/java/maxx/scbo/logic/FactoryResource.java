package maxx.scbo.logic;

import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.scenario.Scenario;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public class FactoryResource extends Resource {
  public ResourceType getType() {
    return ResourceType.FACTORY;
  }
  
  public FactoryResource(Scenario scenario) throws ScboException {
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
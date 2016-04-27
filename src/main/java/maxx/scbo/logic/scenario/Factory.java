package maxx.scbo.logic.scenario;

import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.ResourceType;
import maxx.scbo.logic.config.ConfigProducer;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.LinkedList;

public class Factory extends ConfigProducer {
  @Override
  public void checkValid() {
    super.checkValid();
    assert slots >= 1;
    for (Resource r : getResources()) {
      assert r.getType().equals(ResourceType.FACTORY);
    }
  }
  
  public Factory(Scenario scenario) throws ScboException {
    super(scenario);
    getScenario().setFactory(this);
  }

  /**
   * TODO.
   */
  public LinkedList<LinearConstraint> getConstraints() {
    LinkedList<LinearConstraint> constraints = new LinkedList<LinearConstraint>();
    
    // sum(prodPerMin * prodTim) <= slot
    RealVector coeff = new ArrayRealVector(getScenario().getResourceNo());
    for (Resource r : getResources()) {
      coeff.setEntry(r.getScenarioIdx(), r.getTime());
    }
    
    LinearConstraint maxSlotConstraint = new LinearConstraint(coeff, Relationship.LEQ, getSlots());
    
    constraints.add(maxSlotConstraint);
    
    return constraints;
  }
}

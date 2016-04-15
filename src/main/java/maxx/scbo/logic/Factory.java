package maxx.scbo.logic;

import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.scenario.Resource;
import maxx.scbo.logic.scenario.Scenario;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.LinkedList;

public class Factory extends Producer {
  private int slots;

  public int getSlots() {
    return slots;
  }

  public void setSlots(int slots) {
    this.slots = slots;
  }

  @Override
  public void checkValid() throws ScboException {
    super.checkValid();
    if (slots < 1) {
      throw new ScboException();
    }
    for (Resource r : getResources()) {
      if (! r.getType().equals(ResourceType.FACTORY)) {
        throw new ScboException();
      }
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

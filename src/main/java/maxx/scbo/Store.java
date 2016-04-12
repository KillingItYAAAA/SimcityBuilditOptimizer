package maxx.scbo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.LinkedList;

public class Store extends Producer {
  private String name;

  public String getName() {
    return name;
  }

  /**
   * TODO.
   * 
   * @param name TODO
   * @throws ScboException TODO
   */
  public void setName(String name) throws ScboException {
    if (this.name != null) {
      throw new ScboException();
    }
    this.name = name;
  }
  
  @Override
  public void checkValid() throws ScboException {
    super.checkValid();
    if (name == null || "".equals(name)) {
      throw new ScboException();
    }
    if (getScenario().getStoreByName(getName()) == null) {
      throw new ScboException();
    }
    for (Resource r : getResources()) {
      if (r.getType() != ResourceType.STORE) {
        throw new ScboException();
      }
    }
  }
  
  Store(Scenario scenario, String name) throws ScboException {
    super(scenario);
    setName(name);
    if (scenario.getStoreByName(name) != null) {
      throw new ScboException("multiple store in rules.xml: " + name);
    }
    scenario.addStoreName(this);
  }
  
  /**
   * TODO.
   */
  public LinkedList<LinearConstraint> getConstraints() {
    LinkedList<LinearConstraint> constraints = new LinkedList<LinearConstraint>();
    
    // sum(prodPerMin * prodTim) <= 1
    RealVector coeff = new ArrayRealVector(getScenario().getResourceNo());
    for (Resource r : getResources()) {
      coeff.setEntry(r.getScenarioIdx(), r.getTime());
    }
    
    LinearConstraint maxSlotConstraint = new LinearConstraint(coeff, Relationship.LEQ, 1);
    
    constraints.add(maxSlotConstraint);
    
    return constraints;
    
  }
}
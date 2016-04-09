package maxx.scbo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.LinkedList;

public class FactoryResource extends Resource {
  public ResourceType getType() {
    return ResourceType.STORE;
  }
  
  FactoryResource(Scenario scenario) {
    super(scenario);
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    // at least 0 prod / min
    
    RealVector v = new ArrayRealVector(getScenario().getResourceNo());
    v.setEntry(getIdx(), 1);
    LinearConstraint lc = new LinearConstraint(v, Relationship.GEQ, 0);
    LinkedList<LinearConstraint> list = new LinkedList<LinearConstraint>();
    list.add(lc);
    return list;
  }
}
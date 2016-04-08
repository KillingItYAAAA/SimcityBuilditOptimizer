package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public class FactoryResource extends Resource {
  public ResourceType getType() {
    return ResourceType.STORE;
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    
  }
}
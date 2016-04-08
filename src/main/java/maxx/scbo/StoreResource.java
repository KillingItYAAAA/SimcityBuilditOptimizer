package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public class StoreResource extends Resource {
  private Store store;
  
  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public ResourceType getType() {
    return ResourceType.STORE;
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    
  }
}
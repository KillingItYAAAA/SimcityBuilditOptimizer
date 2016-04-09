package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;
import java.util.TreeMap;

public class StoreResource extends Resource {
  private Store store;
  private TreeMap<Resource, Integer> rawMaterials;
  
  StoreResource(Scenario scenario) {
    super(scenario);
  }
  
  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public ResourceType getType() {
    return ResourceType.STORE;
  }
  
  public void addRaw(Resource raw, int number) throws SCBOException {
    if (rawMaterials.get(raw) != null)
      throw new SCBOException("same resource added twice as raw material: "+raw.getName());
    rawMaterials.put(raw, number);
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    
  }
}
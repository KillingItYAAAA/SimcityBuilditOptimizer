package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;
import java.util.TreeMap;

public class StoreResource extends Resource {
  private Store store;
  private TreeMap<Resource, Integer> rawMaterials;
  
  StoreResource(Scenario scenario) throws ScboException {
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
  
  @Override
  public void checkValid() throws ScboException {
    super.checkValid();
    if (getScenario().getStoreByName(getName()) == null) {
      throw new ScboException();
    }
    for (Resource r : rawMaterials.keySet()) {
      if (!r.isRawFor(this)) {
        throw new ScboException();
      }
      if (r.getScenario() != getScenario()) {
        throw new ScboException();
      }
    }
  }
  
  /**
   * TODO.
   * 
   * @param raw TODO
   * @param number TODO
   * @throws ScboException TODO
   */
  public void addRaw(Resource raw, int number) throws ScboException {
    if (rawMaterials.get(raw) != null) {
      throw new ScboException("same resource added twice as raw material: " + raw.getName());
    }
    rawMaterials.put(raw, number);
  }
  
  public boolean hasRaw(Resource resource) {
    return rawMaterials.containsKey(resource);
  }
  
  @Override
  public LinkedList<LinearConstraint> getConstraints() {
    return super.getConstraints();
  }
}
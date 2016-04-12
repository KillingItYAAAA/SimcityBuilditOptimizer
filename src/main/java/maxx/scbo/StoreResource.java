package maxx.scbo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.LinkedList;
import java.util.TreeMap;

public class StoreResource extends Resource {
  private Store store;
  private TreeMap<Resource, Integer> rawMaterials = new TreeMap<Resource, Integer>();

  StoreResource(Scenario scenario) throws ScboException {
    super(scenario);
  }

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) throws ScboException {
    if (this.store != null) {
      throw new ScboException();
    }
    this.store = store;
    store.addResource(this);
  }

  public ResourceType getType() {
    return ResourceType.STORE;
  }

  @Override
  public void checkValid() throws ScboException {
    super.checkValid();
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
   * @param raw
   *          TODO
   * @param number
   *          TODO
   * @throws ScboException
   *           TODO
   */
  public void addRaw(Resource raw, int number) throws ScboException {
    if (rawMaterials.get(raw) != null) {
      throw new ScboException("same resource added twice as raw material: " + raw.getName());
    }
    rawMaterials.put(raw, number);
    raw.addRawsFor(this);
  }

  public boolean hasRaw(Resource resource) {
    return rawMaterials.containsKey(resource);
  }

  public int getNumRaws(Resource resource) {
    return rawMaterials.get(resource);
  }

  /**
   * Breaks a raw-resource contact.
   * 
   * @param raw
   *          TODO.
   * @throws ScboException
   *           TODO.
   */
  public void removeRaw(Resource raw) throws ScboException {
    if (rawMaterials.get(raw) == null) {
      throw new ScboException("can't remove not registered raw resource");
    }
    rawMaterials.remove(raw);
  }

  @Override
  public LinkedList<LinearConstraint> getConstraints() {
    LinkedList<LinearConstraint> lc = super.getConstraints();

    // VERY DEAD CODE
    if (0 == lc.size()+1) {
      for (Resource raw : rawMaterials.keySet()) {
        // every production rate must be smaller as the prod rate of any of its
        // raw resources
        RealVector vec = new ArrayRealVector(getScenario().getResourceNo());
        vec.setEntry(getScenarioIdx(), -1.0 / getTime());
        vec.setEntry(raw.getScenarioIdx(), 1.0 / raw.getTime() / rawMaterials.get(raw));
        LinearConstraint needMoreRaw = new LinearConstraint(vec, Relationship.GEQ, 0);
        lc.add(needMoreRaw);
      }
    }

    return lc;
  }
}
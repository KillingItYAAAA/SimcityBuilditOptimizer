package maxx.scbo.logic;

import maxx.scbo.helper.Checkable;
import maxx.scbo.helper.ScboException;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * TODO.
 * 
 * @author phorvath
 *
 */
public class Scenario implements Checkable {
  private TreeMap<String, Store> stores = new TreeMap<String, Store>();
  private TreeMap<String, Resource> resources = new TreeMap<String, Resource>();
  private LinkedList<Producer> producers = new LinkedList<Producer>();
  private TreeSet<ConstraintSource> constraintSources = new TreeSet<ConstraintSource>();
  private Factory factory;

  private TreeMap<Integer, Resource> resourcesByIdx = new TreeMap<Integer, Resource>();
  private int resourceNo = 0;

  public Store getStoreByName(String name) {
    return stores.get(name);
  }

  public void addProducer(Producer producer) throws ScboException {
    producers.add(producer);
  }

  public boolean hasProducer(Producer producer) {
    return producers.contains(producer);
  }

  public void addStoreName(Store store) {
    stores.put(store.getName(), store);
  }

  public int getResourceNo() {
    return this.resourceNo;
  }

  /**
   * TODO.
   * 
   * @param resource
   *          the resource to add to the scenario.
   * @throws ScboException
   *           If it doesn't have a name yet
   */
  public void registerResource(Resource resource) throws ScboException {
    if (resources.get(resource.getName()) != null) {
      throw new ScboException("multiple resources in rules.xml: " + resource.getName());
    }
    resources.put(resource.getName(), resource);
    resource.setScenarioIdx(resourceNo);
    resourcesByIdx.put(resourceNo, resource);
    resourceNo++;
  }

  /**
   * Unregisters a resource from a scenario.
   * 
   * @param resource
   *          TODO.
   * @throws ScboException
   *           TODO.
   */
  @Deprecated
  public void unregisterResource(Resource resource) throws ScboException {
    if (resources.get(resource.getName()) == null) {
      throw new ScboException("can only unregister an already registered resource");
    }
    resources.remove(resource.getName());
    resource.setScenarioIdx(0);
    resource.setScenario(null);
  }

  public boolean hasResource(String name) {
    return resources.containsKey(name);
  }

  public void addConstraintSource(ConstraintSource constraintSource) {
    constraintSources.add(constraintSource);
  }

  public boolean hasConstraintSource(ConstraintSource constraintSource) {
    return constraintSources.contains(constraintSource);
  }

  public Resource getResource(String name) {
    return resources.get(name);
  }

  public Resource getResourceByIdx(int idx) {
    return resourcesByIdx.get(idx);
  }

  public Factory getFactory() {
    return factory;
  }

  /**
   * TODO.
   * 
   * @param factory
   *          TODO
   * @throws ScboException
   *           TODO
   */
  public void setFactory(Factory factory) throws ScboException {
    if (this.factory != null) {
      throw new ScboException();
    }
    this.factory = factory;
  }

  @Override
  public void checkValid() throws ScboException {
    if (factory == null) {
      throw new ScboException();
    }

    for (Producer p : producers) {
      p.checkValid();
    }

    for (Resource r : resources.values()) {
      r.checkValid();
    }
  }

  /**
   * TODO.
   * 
   * @return A solution, i.e. maximal money production rate per minute.
   */
  public PointValuePair calculate() {
    // Calculate objective function
    ArrayRealVector coeffs = new ArrayRealVector(getResourceNo());
    for (int idx = 0; idx < getResourceNo(); idx++) {
      Resource resource = getResourceByIdx(idx);
      coeffs = coeffs.add(resource.getSaleVec());
    }
    LinearObjectiveFunction lof = new LinearObjectiveFunction(coeffs, 0);

    // calculate constraints
    LinkedList<LinearConstraint> allConstraints = new LinkedList<LinearConstraint>();
    for (ConstraintSource constraintSource : constraintSources) {
      LinkedList<LinearConstraint> curConstraints = constraintSource.getConstraints();
      allConstraints.addAll(curConstraints);
    }

    // solve the problem
    SimplexSolver solver = new SimplexSolver();
    /*
     * for (Object lco : allConstraints.toArray()) { LinearConstraint lc =
     * (LinearConstraint) lco;
     * System.err.println(lc.getCoefficients().toString() + " " +
     * lc.getRelationship().toString() + " " + lc.getValue()); }
     */
    PointValuePair solution = solver.optimize(new MaxIter(1000000), lof,
        new LinearConstraintSet(allConstraints), GoalType.MAXIMIZE);
    return solution;
  }
}
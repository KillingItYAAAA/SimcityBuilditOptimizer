package maxx.scbo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
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
  private ArrayList<Resource> resourceList;
  private LinkedList<Producer> producers = new LinkedList<Producer>();
  private TreeSet<ConstraintSource> constraintSources = new TreeSet<ConstraintSource>();
  private Factory factory;

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

  /**
   * TODO.
   * 
   * @param resource the resource to add to the scenario.
   * @throws ScboException If it doesn't have a name yet
   */
  public void addResource(Resource resource) throws ScboException {
    if (resources.get(resource.getName()) != null) {
      throw new ScboException("multiple resources in rules.xml: " + resource.getName());
    }
    resources.put(resource.getName(), resource);
    int idx = resourceNo++;
    resourceList.add(idx, resource);
    resource.setIdx(idx);
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

  public int getResourceNo() {
    return resourceNo;
  }

  public Factory getFactory() {
    return factory;
  }

  /**
   * TODO.
   * 
   * @param factory TODO
   * @throws ScboException TODO
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
    ArrayRealVector coeffs = new ArrayRealVector(50);
    for (Resource resource : resourceList) {
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
    PointValuePair solution = solver.optimize(new MaxIter(100), lof,
        new LinearConstraintSet(allConstraints), GoalType.MAXIMIZE);
    return solution;
  }
}

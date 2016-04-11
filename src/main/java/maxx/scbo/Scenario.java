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
 * @author phorvath
 *
 */
public class Scenario implements Checkable {
  private TreeMap<String, Store> stores;
  private TreeMap<String, Resource> resources;
  private ArrayList<Resource> resourceList;
  private LinkedList<Producer> producers;
  private TreeSet<ConstraintSource> constraintSources;
  private Factory factory;

  private int resourceNo = 0;

  public Store getStoreByName(String name) {
    return stores.get(name);
  }

  public void addProducer(Producer producer) throws SCBOException {
    producers.add(producer);
  }

  public boolean hasProducer(Producer producer) {
    return producers.contains(producer);
  }

  public void addStoreName(Store store) {
    stores.put(store.getName(), store);
  }

  public void addResource(Resource resource) throws SCBOException {
    if (resources.get(resource.getName()) != null)
      throw new SCBOException("multiple resources in rules.xml: " + resource.getName());
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

  public void setFactory(Factory factory) throws SCBOException {
    if (this.factory != null)
      throw new SCBOException();
    this.factory = factory;
  }

  @Override
  public void checkValid() throws SCBOException {
    if (factory == null)
      throw new SCBOException();

    for (Producer p : producers)
      p.checkValid();

    for (Resource r : resources.values())
      r.checkValid();
  }

  public PointValuePair calculate() {
    // Calculate objective function
    ArrayRealVector coeffs = new ArrayRealVector(50);
    for (Resource resource : resourceList) {
      coeffs = coeffs.add(resource.getSaleVec());
    }
    LinearObjectiveFunction l = new LinearObjectiveFunction(coeffs, 0);

    // calculate constraints
    LinkedList<LinearConstraint> constraints = new LinkedList<LinearConstraint>();
    for (ConstraintSource constraintSource : constraintSources) {
      LinkedList<LinearConstraint> c = constraintSource.getConstraints();
      constraints.addAll(c);
    }

    // solve the problem
    SimplexSolver solver = new SimplexSolver();
    PointValuePair solution = solver.optimize(new MaxIter(100), l,
        new LinearConstraintSet(constraints), GoalType.MAXIMIZE);
    return solution;
  }
}
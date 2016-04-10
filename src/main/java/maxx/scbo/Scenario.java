package maxx.scbo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;

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

  @Override
  public void checkValid() throws SCBOException {
    for (Producer p : producers)
      p.checkValid();
    for (Resource r : resources.values())
      r.checkValid();
  }

  public PointValuePair calculate() {
    ArrayRealVector coeffs = new ArrayRealVector(50);
    for (Resource resource : resourceList) {
      coeffs.addToEntry(resource.getIdx(), resource.getValue());
      coeffs.setEntry(resource.getIdx(), resource.getValue());
    }
    LinearObjectiveFunction l = new LinearObjectiveFunction(coeffs, 0);
    LinkedList<LinearConstraint> constraints = new LinkedList<LinearConstraint>();
    for (ConstraintSource constraintSource : constraintSources) {
      LinkedList<LinearConstraint> c = constraintSource.getConstraints();
      constraints.addAll(c);
    }
  }
}
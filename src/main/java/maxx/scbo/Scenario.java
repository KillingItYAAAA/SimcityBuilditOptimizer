package maxx.scbo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
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
public class Scenario {
  private TreeMap<String, Store> stores;
  private TreeMap<String, Resource> resources;
  private ArrayList<Resource> resourceList;
  private LinkedList<Producer> producers;
  private TreeSet<FactoryResource> factoryResources;
  
  private int resourceNo = 0;

  public void addStore(Store store) throws SCBOException {
    if (stores.get(store.getName()) != null)
      throw new SCBOException("multiple store in rules.xml: "+store.getName());
    stores.put(store.getName(), store);
  }
  
  public Store getStore(String name) {
    return stores.get(name);
  }
  
  public void addProducer(Producer producer) {
    producers.add(producer);
  }
  
  public void addResource(Resource resource) throws SCBOException {
    if (resources.get(resource.getName()) != null)
      throw new SCBOException("multiple resources in rules.xml: "+resource.getName());
    resources.put(resource.getName(), resource);
    int idx = resourceNo++;
    resourceList.add(idx, resource);
    resource.setIdx(idx);
    
    if (resource.getType() == ResourceType.FACTORY)
      factoryResources.add((FactoryResource)resource);
  }

  public Resource getResource(String name) {
    return resources.get(name);
  }

  public int getResourceNo() {
    return resourceNo;
  }

  public LinearObjectiveFunction getLinearObjectiveFunction() {
    RealVector coeffs = new ArrayRealVector(resourceNo);
    for (Resource resource : resourceList) {
      coeffs.setEntry(resource.getIdx(), resource.getValue());
    }
    LinearObjectiveFunction l = new LinearObjectiveFunction(coeffs, 0);
    return l;
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    return new LinkedList<LinearConstraint>();
  }
}
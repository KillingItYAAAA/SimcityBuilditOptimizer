package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * @author phorvath
 *
 */
public class Scenario {
  private TreeMap<String, Store> stores;
  private TreeMap<String, Resource> resources;
  private LinkedList<Producer> producers;

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
  }

  public Resource getResource(String name) {
    return resources.get(name);
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    return new LinkedList<LinearConstraint>();
  }
}
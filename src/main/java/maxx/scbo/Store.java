package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public class Store extends Producer {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  Store(Scenario scenario, String name) throws SCBOException {
    super(scenario);
    setName(name);
    if (scenario.getStoreByName(name) != null)
      throw new SCBOException("multiple store in rules.xml: "+name);
    scenario.addStoreName(this);
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    
  }
}
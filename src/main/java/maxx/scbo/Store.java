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
  
  Store(String name) {
    setName(name);
  }

  public LinkedList<LinearConstraint> getConstraints() {
    
  }
}
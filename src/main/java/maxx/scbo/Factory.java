package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public class Factory extends Producer {
  private int slots;

  public int getSlots() {
    return slots;
  }

  public void setSlots(int slots) {
    this.slots = slots;
  }

  public Factory(Scenario scenario) {
    super(scenario);
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    
  }
}
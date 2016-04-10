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

  @Override
  public void checkValid() throws SCBOException {
    super.checkValid();
    if (slots < 1)
      throw new SCBOException();
  }
  
  public Factory(Scenario scenario) throws SCBOException {
    super(scenario);
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    
  }
}
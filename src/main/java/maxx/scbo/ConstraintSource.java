package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public abstract class ConstraintSource implements Checkable {
  private Scenario scenario;
  
  public abstract LinkedList<LinearConstraint> getConstraints();
  
  public ConstraintSource(Scenario scenario) {
    this.scenario = scenario;
    scenario.addConstraintSource(this);
  }
  
  public void checkValid() throws SCBOException {
    if (scenario == null || !scenario.hasConstraintSource(this))
      throw new SCBOException();
  }
  
  public Scenario getScenario() {
    return scenario;
  }
}
package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public abstract class ConstraintSource {
  private Scenario scenario;
  
  public abstract LinkedList<LinearConstraint> getConstraints();
  
  public ConstraintSource(Scenario scenario) {
    this.scenario = scenario;
  }
  
  public Scenario getScenario() {
    return scenario;
  }
}
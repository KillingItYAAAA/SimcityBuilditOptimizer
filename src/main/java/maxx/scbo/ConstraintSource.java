package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public abstract class ConstraintSource implements Checkable, Comparable<ConstraintSource> {
  private Id id = new Id();

  private Scenario scenario;
  
  public abstract LinkedList<LinearConstraint> getConstraints();
  
  public ConstraintSource(Scenario scenario) {
    this.scenario = scenario;
    scenario.addConstraintSource(this);
  }
  
  /**
   * TODO.
   */
  public void checkValid() throws ScboException {
    if (scenario == null || !scenario.hasConstraintSource(this)) {
      throw new ScboException();
    }
  }
  
  public Scenario getScenario() {
    return scenario;
  }

  public Integer getId() {
    return (Integer)id.getId();
  }

  @Override
  public int compareTo(ConstraintSource constraintSource) {
    return constraintSource.getId().compareTo(getId());
  }

}

package maxx.scbo.logic;

import maxx.scbo.helper.Checkable;
import maxx.scbo.helper.Id;
import maxx.scbo.helper.ScboException;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public abstract class ConstraintSource implements Checkable, Comparable<ConstraintSource> {
  private Id id;

  private Scenario scenario;

  public abstract LinkedList<LinearConstraint> getConstraints();

  /**
   * TODO.
   * 
   * @param scenario TODO.
   * @throws ScboException TODO.
   */
  public ConstraintSource(Scenario scenario) throws ScboException {
    id = new Id();
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

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public Integer getId() {
    return (Integer) id.getId();
  }

  @Override
  public int compareTo(ConstraintSource constraintSource) {
    return constraintSource.getId().compareTo(getId());
  }
}
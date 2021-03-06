package maxx.scbo.logic.scenario;

import maxx.scbo.helper.Checkable;
import maxx.scbo.helper.Id;
import maxx.scbo.helper.ScboException;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public abstract class ConstraintSource extends Id implements Checkable {
  private Scenario scenario;

  public abstract LinkedList<LinearConstraint> getConstraints();

  /**
   * TODO.
   * 
   * @param scenario
   *          TODO.
   * @throws ScboException
   *           TODO.
   */
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

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }
}
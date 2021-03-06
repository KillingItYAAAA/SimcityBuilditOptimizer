package maxx.scbo.logic.scenario;

import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.ResourceType;
import maxx.scbo.logic.config.ConfigProducer;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.LinkedList;

public class Store extends Producer {
  private String name;
  private int level = 0;
  static final double[] levelMultiplier = new double[] { 1.0, 0.9, 0.85, 0.8 };

  public String getName() {
    return name;
  }

  /**
   * TODO.
   * 
   * @param name
   *          TODO
   * @throws ScboException
   *           TODO
   */
  public void setName(String name) throws ScboException {
    assert (this.name != null);
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public double getLevelMultiplier() {
    return levelMultiplier[level];
  }

  @Override
  public void checkValid() {
    super.checkValid();

    assert name != null;
    assert !name.equals("");

    assert getScenario().getStoreByName(getName()) != null;

    for (Resource r : getResources()) {
      assert r.getType() == ResourceType.STORE;
    }
  }

  /**
   * TODO.
   * 
   * @param scenario
   *          TODO
   * @param name
   *          TODO
   * @throws ScboException
   *           TODO
   */
  public Store(Scenario scenario, String name) throws ScboException {
    super(scenario);
    setName(name);
    if (scenario.getStoreByName(name) != null) {
      throw new ScboException("multiple store in rules.xml: " + name);
    }
    scenario.addStoreName(this);
  }

  /**
   * TODO.
   */
  public LinkedList<LinearConstraint> getConstraints() {
    LinkedList<LinearConstraint> constraints = new LinkedList<LinearConstraint>();

    // sum(prodPerMin * prodTim) <= 1
    RealVector coeff = new ArrayRealVector(getScenario().getResourceNo());
    for (Resource r : getResources()) {
      coeff.setEntry(r.getScenarioIdx(), r.getTime());
    }

    LinearConstraint maxSlotConstraint = new LinearConstraint(coeff, Relationship.LEQ, 1);

    constraints.add(maxSlotConstraint);

    return constraints;

  }

  public double getProdAccel() {
    return getConfiguration().getAccelForLevel(getLevel()) * (24 + getDailyTempomark()) / 24;
  }
}
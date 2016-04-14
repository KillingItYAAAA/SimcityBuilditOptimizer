package maxx.scbo.logic;

import maxx.scbo.helper.Id;
import maxx.scbo.helper.ScboException;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.LinkedList;
import java.util.TreeSet;

public abstract class Resource extends ConstraintSource {
  private String name;
  private double time;
  private int level;
  private double value;
  private TreeSet<StoreResource> rawsFor = new TreeSet<StoreResource>();

  private Id scenarioIdx;
  private double prodPerMin; // calculated result

  public Resource(Scenario scenario) throws ScboException {
    super(scenario);
    scenarioIdx = scenario.getIdFactory().get();
  }

  public int getScenarioIdx() {
    return scenarioIdx.getId();
  }

  public void setScenarioIdx(int scenarioIdx) {
    this.scenarioIdx = scenarioIdx;
  }

  public double getProdPerMin() {
    return prodPerMin;
  }

  public void setProdPerMin(double prodPerMin) {
    this.prodPerMin = prodPerMin;
  }

  public String getName() {
    return name;
  }

  /**
   * Sets the name of a resource. It _must_ have one. Giving name to the
   * resource it also registers it in its scenario (where it is identified
   * partially by its name).
   * 
   * @param name
   *          Name of the resource. May be set only once in its lifetime.
   * @throws ScboException
   *           In case of a second name change it will be throw.
   */
  public void setName(String name) throws ScboException {
    if (this.name != null) {
      throw new ScboException();
    }

    this.name = name;
    getScenario().registerResource(this);
  }

  public double getTime() {
    return time;
  }

  public void setTime(double time) {
    this.time = time;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public TreeSet<StoreResource> getRawsFor() {
    return rawsFor;
  }

  public boolean isRawFor(StoreResource storeResource) {
    return rawsFor.contains(storeResource);
  }

  public void addRawsFor(StoreResource storeResource) {
    rawsFor.add(storeResource);
  }

  @Override
  public void checkValid() throws ScboException {
    super.checkValid();
    if ((name == null) || (time <= 0) || (level == 0) /*|| (value == 0)*/) {
      throw new ScboException("Resource validation check failed");
    }
    for (StoreResource rawFor : rawsFor) {
      if (!rawFor.hasRaw(this)) {
        throw new ScboException();
      }
      if (rawFor.getScenario() != getScenario()) {
        throw new ScboException();
      }
    }
  }

  /**
   * TODO.
   */
  public LinkedList<LinearConstraint> getConstraints() {
    LinkedList<LinearConstraint> lc = new LinkedList<LinearConstraint>();

    // Every production rate must be at least the sum of the production rates of its
    // target products (multiplied by its usage).
    //
    // This rule also contains, that every production rate must be at least 0.
    //
    // It also contains, that every production rate must be smaller as the rate of any
    // of its raw products.
    RealVector vec = new ArrayRealVector(getScenario().getResourceNo());
    vec.setEntry(getScenarioIdx(), 1);
    
    for (Resource r : getRawsFor()) {
      int usage = ((StoreResource)r).getNumRaws(this);
      vec.setEntry(r.getScenarioIdx(), -usage);
    }
    
    LinearConstraint atLeastTargets = new LinearConstraint(vec, Relationship.GEQ, 0);
    lc.add(atLeastTargets);

    return lc;
  }

  public abstract ResourceType getType();

  /**
   * TODO.
   * 
   * @return The Sale Vector - essentially, the produced resources per minute
   *         _for_ _sale_, i.e. after the removal of the production rate of its
   *         "parent" resources, i.e. whose raw materials is it
   */
  public RealVector getSaleVec() {
    RealVector coeff = new ArrayRealVector(getScenario().getResourceNo());
    coeff.setEntry(getScenarioIdx(), 1);
    for (Resource r : getRawsFor()) {
      int usage = ((StoreResource)r).getNumRaws(this);
      coeff.setEntry(r.getScenarioIdx(), -usage);
    }
    coeff = coeff.mapMultiply(getValue());
    return coeff;
  }
}
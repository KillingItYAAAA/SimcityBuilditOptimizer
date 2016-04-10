package maxx.scbo;

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
  private TreeSet<StoreResource> rawsFor;

  private int idx;
  private double prodPerMin;

  public Resource(Scenario scenario) {
    super(scenario);
  }
  
  public int getIdx() {
    return idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
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

  public void setName(String name) {
    this.name = name;
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
  
  public boolean isRawFor(StoreResource r) {
    return rawsFor.contains(r);
  }
  
  public void addRawsFor(StoreResource storeResource) {
    rawsFor.add(storeResource);
  }
  
  @Override
  public void checkValid() throws SCBOException {
    super.checkValid();
    if ((name == null) || (time <= 0) || (level == 0) || (value == 0))
      throw new SCBOException("Resource validation check failed");
    for (StoreResource rawFor : rawsFor) {
      if (!rawFor.hasRaw(this))
        throw new SCBOException();
    }
  }

  public LinkedList<LinearConstraint> getConstraints() {
    RealVector vec = new ArrayRealVector(getScenario().getResourceNo());
    vec.setEntry(getIdx(), 1);
    LinearConstraint atLeastZero = new LinearConstraint(vec, Relationship.GEQ, 0); 
    LinkedList<LinearConstraint> lc = new LinkedList<LinearConstraint>();
    lc.add(atLeastZero);
    return lc;
  }
  
  public abstract ResourceType getType();
}
package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

public abstract class Resource {
  private String name;
  private double time;
  private int level;
  private double value;
  
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

  public void testValid() throws SCBOException {
    if ((name == null) || (time <= 0) || (level == 0) || (value == 0))
      throw new SCBOException("Resource validation check failed");
  }
  
  public abstract ResourceType getType();
  public abstract LinkedList<LinearConstraint> getConstraints();
}
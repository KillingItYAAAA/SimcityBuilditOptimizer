package maxx.scbo;

import org.apache.commons.math3.optim.linear.LinearConstraint;

import java.util.LinkedList;

/**
 * @author phorvath
 *
 */
public class Scenario {
  private LinkedList<Producer> producers;
  
  public void addProducer(Producer producer) {
    producers.add(producer);
  }
  
  public LinkedList<LinearConstraint> getConstraints() {
    return new LinkedList<LinearConstraint>();
  }
}
package maxx.scbo;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author phorvath
 */
public class App {
  public static void mainTest(String[] args) {
    System.out.println("Hello World!");

    LinearObjectiveFunction l = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);

    Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

    constraints.add(new LinearConstraint(new double[] { 1, 0 }, Relationship.GEQ, 0));
    constraints.add(new LinearConstraint(new double[] { 0, 1 }, Relationship.GEQ, 0));
    constraints.add(new LinearConstraint(new double[] { 1, 0 }, Relationship.LEQ, 1));
    constraints.add(new LinearConstraint(new double[] { 0, 1 }, Relationship.LEQ, 1));

    SimplexSolver solver = new SimplexSolver();
    PointValuePair optSolution = solver.optimize(new MaxIter(100), l, new LinearConstraintSet(constraints),
        GoalType.MAXIMIZE, new NonNegativeConstraint(true));
    
    System.out.println("Solution:\nValue: "+optSolution.getValue()+" at "+Arrays.toString(optSolution.getKey()));
  }
  
  public static void main(String[] args) {
    ConfigLoader configLoader = new ConfigLoader();
    Scenario scenario = new Scenario();
    try {
      configLoader.loadInto(scenario);
      Factory factory = new Factory();
      factory.setSlots(50);
      scenario.addProducer(factory);
    } catch (SCBOException e) {
      System.err.println("Fatal internal error: "+e.getMessage());
    }
    PointValuePair solution = scenario.calculate();
    System.out.println("Solution:\nValue: "+solution.getValue()+" at "+Arrays.toString(solution.getKey()));
  }
}
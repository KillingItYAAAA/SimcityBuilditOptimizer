package maxx.scbo.clui;

import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.config.ConfigLoader;
import maxx.scbo.logic.scenario.Factory;
import maxx.scbo.logic.scenario.Resource;
import maxx.scbo.logic.scenario.Scenario;
import maxx.scbo.logic.scenario.StoreResource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * TODO.
 * 
 * @author phorvath
 */
public class App {
  /**
   * TODO.
   * 
   * @param args
   *          Command line arguments
   */
  public static void mainTest(String[] args) {
    System.out.println("Hello World!");

    LinearObjectiveFunction lof = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);

    Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

    constraints.add(new LinearConstraint(new double[] { 1, 0 }, Relationship.GEQ, 0));
    constraints.add(new LinearConstraint(new double[] { 0, 1 }, Relationship.GEQ, 0));
    constraints.add(new LinearConstraint(new double[] { 1, 0 }, Relationship.LEQ, 1));
    constraints.add(new LinearConstraint(new double[] { 0, 1 }, Relationship.LEQ, 1));

    SimplexSolver solver = new SimplexSolver();
    PointValuePair optSolution = solver.optimize(new MaxIter(100), lof,
        new LinearConstraintSet(constraints), GoalType.MAXIMIZE, new NonNegativeConstraint(true));

    System.out.println("Solution:\nValue: " + optSolution.getValue() + " at "
        + Arrays.toString(optSolution.getKey()));
  }

  /**
   * TODO.
   * 
   * @param args
   *          Command line arguments
   */
  public static void main(String[] args) {
    try {
      Scenario scenario = new Scenario();
      ConfigLoader configLoader = new ConfigLoader(scenario);
      Factory factory = new Factory(scenario);
      factory.setSlots(50);
      configLoader.loadInto();
      scenario.getStoreByName("Building Supply").setLevel(3);
      scenario.getStoreByName("Hardware Store").setLevel(3);
      scenario.getStoreByName("Farmer's Market").setLevel(3);
      scenario.getStoreByName("Furniture Store").setLevel(3);
      scenario.getStoreByName("Gardening Supplies").setLevel(2);
      scenario.getStoreByName("Donut Shop").setLevel(2);
      scenario.getStoreByName("Fashion Store").setLevel(1);
      scenario.getStoreByName("Fast Food Restaurant").setLevel(1);
      scenario.getStoreByName("Home Appliances").setLevel(1);
      scenario.checkValid();

      PointValuePair solution = scenario.calculate();
      // System.out.println("Solution:\nValue: " + solution.getValue() + " at "
      // + Arrays.toString(solution.getKey()));

      double[] result = solution.getKey();
      DecimalFormat df = new DecimalFormat("#.#####");
      
      double sum = 0;
      
      for (int i = 0; i < scenario.getResourceNo(); i++) {
        if (Math.abs(result[i]) < 0.0001) {
          continue;
        }

        Resource resource = scenario.getResourceByIdx(i);
        String resName = resource.getName();
        String produced = df.format(result[i] * 1440);
        LinkedList<String> outDesc = new LinkedList<String>();
        double left = result[i];
        for (StoreResource tgtRes : resource.getRawsFor()) {
          int usage = tgtRes.getNumRaws(resource);
          int tgtIdx = tgtRes.getScenarioIdx();
          if (Math.abs(result[tgtIdx]) < 0.0001) {
            continue;
          }
          String tgtUsed = df.format(result[tgtIdx] * usage * 1440);
          outDesc.add("(" + tgtRes.getName() + "->" + tgtUsed + ")");
          left -= result[tgtIdx] * usage;
        }

        double sold = left * resource.getValue();

        String outText = StringUtils.join(outDesc, ", ");
        System.err.println(resName + ": " + produced + " " + outText + " left: "
            + df.format(left * 1440) + " (" + df.format(sold * 1440) + ")");
        
        sum += sold;
      }

      System.err.println("Daily sales: " + df.format(sum * 1440));
      
    } catch (ScboException exception) {
      System.err.println("Fatal internal error: " + exception.getMessage());
      throw new RuntimeException(exception);
    }
  }
}
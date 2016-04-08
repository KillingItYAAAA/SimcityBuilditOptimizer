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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

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
  
  private static void resourceLoader() throws SCBOException {
    ClassLoader classLoader = App.class.getClassLoader();
    File file = new File(classLoader.getResource("rules.xml").getFile());
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser;
    try {
      saxParser = factory.newSAXParser();
    } catch (SAXException | ParserConfigurationException e) {
      throw new SCBOException("rules.xml resource invalid");
    }
    
  }
  
  public static void main(String[] args) {
    try {
      resourceLoader();
    } catch (SCBOException e) {
      System.err.println("Fatal internal error: "+e.getMessage());
    }
  }
}
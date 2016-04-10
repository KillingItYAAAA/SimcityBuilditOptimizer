package maxx.scbo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.TreeSet;

public class LinearConstraintFactory {
  public static LinearConstraint resource(TreeSet<Resource> resources, Relationship r, double value) throws SCBOException {
    if (resources.isEmpty())
      throw new SCBOException();
    Scenario s = resources.first().getScenario();
    RealVector vec = new ArrayRealVector(s.getResourceNo());
    
    for (Resource resource : resources) {
      if (resource.getScenario() != s)
        throw new SCBOException();
      vec.setEntry(resource.getIdx(), resource.getValue());
    }
  }
  
  public static LinearConstraint resource(Resource resource) throws SCBOException {
    return resource(TreeSet<Resource>(resource));
  }
}

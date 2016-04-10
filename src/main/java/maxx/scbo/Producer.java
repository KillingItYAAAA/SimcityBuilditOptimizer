package maxx.scbo;

import java.util.LinkedList;

public abstract class Producer extends ConstraintSource {
  private LinkedList<Resource> resources;

  public Producer(Scenario scenario) throws SCBOException {
    super(scenario);
    scenario.addProducer(this);
  }
  
  public void addResource(Resource resource) {
    resources.add(resource);
  }
  
  public LinkedList<Resource> getResources() {
    return resources;
  }
}
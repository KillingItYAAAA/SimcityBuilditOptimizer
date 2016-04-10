package maxx.scbo;

import java.util.LinkedList;

public abstract class Producer extends ConstraintSource {
  private LinkedList<Resource> resources;

  public Producer(Scenario scenario) throws SCBOException {
    super(scenario);
    scenario.addProducer(this);
  }
  
  @Override
  public void checkValid() throws SCBOException {
    super.checkValid();
    if (!getScenario().hasProducer(this))
      throw new SCBOException();
  }
  
  public void addResource(Resource resource) {
    resources.add(resource);
  }
  
  public LinkedList<Resource> getResources() {
    return resources;
  }
}
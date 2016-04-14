package maxx.scbo.logic;

import maxx.scbo.helper.ScboException;

import java.util.LinkedList;

public abstract class Producer extends ConstraintSource {
  private LinkedList<Resource> resources = new LinkedList<Resource>();

  public Producer(Scenario scenario) throws ScboException {
    super(scenario);
    scenario.addProducer(this);
  }
  
  @Override
  public void checkValid() throws ScboException {
    super.checkValid();
    if (!getScenario().hasProducer(this)) {
      throw new ScboException();
    }
  }
  
  public void addResource(Resource resource) {
    resources.add(resource);
  }
  
  public LinkedList<Resource> getResources() {
    return resources;
  }
}
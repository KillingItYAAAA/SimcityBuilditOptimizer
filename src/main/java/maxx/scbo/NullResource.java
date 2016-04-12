package maxx.scbo;

public class NullResource extends Resource {
  NullResource(Scenario scenario, String name) throws ScboException {
    super(scenario);
    setName(name);
  }
  
  public void castTo(Resource resource) throws ScboException {
    getScenario().unregisterResource(this);
    getScenario().registerResource(resource);
    for (StoreResource rawFor : getRawsFor()) {
      int numRaw = rawFor.getNumRaws(resource);
      rawFor.removeRaw(this);
      rawFor.addRaw(resource, numRaw);
    }
  }
  
  @Override
  public ResourceType getType() {
    return ResourceType.NULL;
  }
  
  @Override
  public void checkValid() throws ScboException {
    throw new ScboException();
  }
}
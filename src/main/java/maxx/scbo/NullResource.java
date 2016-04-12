package maxx.scbo;

public class NullResource extends Resource {
  NullResource(Scenario scenario, String name) throws ScboException {
    super(scenario);
    setName(name);
  }
  
  /**
   * Cast a (pre-declared), temporary Null-Resource to a real one.
   * 
   * @param resource TODO.
   * @throws ScboException TODO.
   */
  public void castTo(Resource resource) throws ScboException {
    Scenario scenario = getScenario();
    scenario.unregisterResource(this);
    scenario.registerResource(resource);
    for (StoreResource rawFor : getRawsFor()) {
      int numRaw = rawFor.getNumRaws(this);
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
package maxx.scbo;

import java.util.LinkedList;

public abstract class Producer {
  private LinkedList<Resource> resources;
  
  public void addResource(Resource resource) {
    resources.add(resource);
  }
  
  public LinkedList<Resource> getResources() {
    return resources;
  }
}
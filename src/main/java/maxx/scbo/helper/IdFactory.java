package maxx.scbo.helper;

public class IdFactory {
  private static IdFactory globalIdFactory = new IdFactory();
  private int nextId = 0;

  public static Id getGlobalId() throws ScboException {
    return globalIdFactory.get();
  }
  
  public Id get() throws ScboException {
    Id id = new Id(nextId++);
    return id;
  }
  
  public int getNextId() {
    return nextId;
  }
}
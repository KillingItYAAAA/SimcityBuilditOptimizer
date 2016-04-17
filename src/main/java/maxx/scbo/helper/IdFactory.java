package maxx.scbo.helper;

public class IdFactory {
  private static IdFactory globalIdFactory = new IdFactory();
  private int nextId = 0;

  public static Id getGlobalId() {
    return globalIdFactory.get();
  }
  
  public Id get() {
    Id id = new Id(nextId++);
    return id;
  }
  
  public int getNextId() {
    return nextId;
  }
}
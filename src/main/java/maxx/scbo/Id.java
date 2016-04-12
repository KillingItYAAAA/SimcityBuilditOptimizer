package maxx.scbo;

public class Id {
  private static int lastId = 0;
  private final int id = ++lastId;

  public int getId() {
    return id;
  }
};

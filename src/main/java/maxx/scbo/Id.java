package maxx.scbo;

import org.apache.commons.lang3.mutable.MutableInt;

public class Id {
  private static MutableInt globalLastId = new MutableInt(0);
  private MutableInt lastId = null;
  private int id;

  public Id(MutableInt lastId) throws ScboException {
    setLastId(lastId);
  }

  public Id() throws ScboException {
    setLastId(globalLastId);
  }

  public int getId() {
    return id;
  }

  public int getLastId() {
    return lastId.intValue();
  }

  private void setLastId(MutableInt lastId) throws ScboException {
    if (this.lastId != null) {
      throw new ScboException();
    }
    this.lastId = lastId;
    id = lastId.intValue();
    lastId.add(1);
  }
}
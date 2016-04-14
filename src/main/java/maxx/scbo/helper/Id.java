package maxx.scbo.helper;

public class Id {
  private int id;

  public Id(IdFactory idFactory) throws ScboException {
    setId(idFactory.get().getId());
  }
  
  public Id() throws ScboException {
    setId(IdFactory.getGlobalId().getId());
  }

  public Id(int id) {
    setId(id);
  }
  
  public int getId() {
    return id;
  }
  
  private void setId(int id) {
    this.id = id;
  }
}
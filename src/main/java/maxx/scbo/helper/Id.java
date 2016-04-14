package maxx.scbo.helper;

public class Id implements Comparable<Id> {
  private Integer id;

  public Id(IdFactory idFactory) throws ScboException {
    setId(idFactory.get().getId());
  }
  
  public Id() throws ScboException {
    setId(IdFactory.getGlobalId().getId());
  }

  public Id(int id) {
    setId(id);
  }
  
  public Integer getId() {
    return id;
  }
  
  private void setId(int id) {
    this.id = new Integer(id);
  }
  
  @Override
  public int compareTo(Id id) {
    return getId().compareTo(id.getId());
  }
}
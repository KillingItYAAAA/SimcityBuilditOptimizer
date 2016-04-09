package maxx.scbo;

public class Store extends Producer {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  Store(String name) {
    setName(name);
  }
}
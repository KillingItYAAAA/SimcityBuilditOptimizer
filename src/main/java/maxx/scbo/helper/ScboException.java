package maxx.scbo.helper;

public class ScboException extends Exception {
  static final long serialVersionUID = 0x2b812aa1;
  
  public ScboException(String message) {
    super(message);
    throw new RuntimeException("MaXX: " + message);
  }
  
  public ScboException(Exception exception) {
    throw new RuntimeException(exception);
  }

  public ScboException() {
    super();
  }
}
package maxx.scbo;

public class ScboException extends Exception {
  static final long serialVersionUID = 0x2b812aa1;
  
  ScboException(String message) {
    super(message);
    throw new RuntimeException("MaXX: " + message);
  }
  
  ScboException(Exception exception) {
    throw new RuntimeException(exception);
  }

  ScboException() {
    super("");
  }
}

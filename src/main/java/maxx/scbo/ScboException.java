package maxx.scbo;

public class ScboException extends Exception {
  static final long serialVersionUID = 0x2b812aa1;
  
  ScboException(String message) {
    super(message);
  }
  
  ScboException() {
    super("");
  }
}
/**
 * 
 */

package maxx.scbo.helper;

/**
 * TODO.
 * 
 * @author phorvath
 */
public interface Generator {
  public boolean step();
  
  public Object get();
  
  public void reset();
}

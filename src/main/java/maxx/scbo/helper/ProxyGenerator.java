/**
 * 
 */
package maxx.scbo.helper;

/**
 * @author phorvath
 *
 */
public class ProxyGenerator implements Generator {
  private Generator generator;

  public ProxyGenerator() {
  }

  public ProxyGenerator(Generator generator) {
    setGenerator(generator);
  }

  public Generator getGenerator() {
    return generator;
  }

  public void setGenerator(Generator generator) {
    this.generator = generator;
  }

  /*
   * (non-Javadoc)
   * 
   * @see maxx.scbo.helper.Generator#step()
   */
  public boolean step() {
    return generator.step();
  }

  /*
   * (non-Javadoc)
   * 
   * @see maxx.scbo.helper.Generator#get()
   */
  public Object get() {
    return generator.get();
  }

  /*
   * (non-Javadoc)
   * 
   * @see maxx.scbo.helper.Generator#reset()
   */
  public void reset() {
    generator.reset();
  }
}
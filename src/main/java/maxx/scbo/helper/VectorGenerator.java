package maxx.scbo.helper;

/**
 * TODO.
 * 
 * @author phorvath
 */
public class VectorGenerator implements Generator {
  private Generator[] generators;
  private Object[] state;

  /**
   * TODO.
   * 
   * @param generators
   *          TODO.
   */
  public VectorGenerator(Generator[] generators) {
    this.generators = new Generator[generators.length];
    this.state = new Object[generators.length];
    for (int i = 0; i < generators.length; i++) {
      this.generators[i] = generators[i];
    }
  }

  /**
   * TODO.
   */
  public boolean step() {
    int idx;
    for (idx = generators.length - 1; idx >= 0; idx--) {
      if (generators[idx].step()) {
        state[idx] = generators[idx].get();
        return true;
      }
      generators[idx].reset();
    }
    return false;
  }

  public Object[] get() {
    return state;
  }

  /**
   * TODO.
   */
  public void reset() {
    for (int i = 0; i < generators.length - 1; i++) {
      generators[i].reset();
      state[i] = generators[i].get();
    }
  }
}
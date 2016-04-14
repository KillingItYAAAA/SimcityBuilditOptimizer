package maxx.scbo.helper;

/**
 * @author phorvath
 *
 */
public class VectorGenerator implements Generator {
  private Generator[] generators;
  private Object[] state;

  public VectorGenerator(Generator[] generators) {
    this.generators = new Generator[generators.length];
    this.state = new Object[generators.length];
    for (int i = 0; i < generators.length; i++) {
      this.generators[i] = generators[i];
    }
  }

  public boolean step() {
    int i;
    for (i = generators.length - 1; i >= 0; i--) {
      if (generators[i].step()) {
        state[i] = generators[i].get();
        return true;
      }
      generators[i].reset();
    }
    return false;
  }

  public Object[] get() {
    return state;
  }

  public void reset() {
    for (int i = 0; i < generators.length - 1; i++) {
      generators[i].reset();
      state[i] = generators[i].get();
    }
  }
}
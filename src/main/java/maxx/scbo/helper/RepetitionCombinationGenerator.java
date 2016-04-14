/**
 * 
 */
package maxx.scbo.helper;

/**
 * TODO.
 * 
 * @author phorvath
 */
public class RepetitionCombinationGenerator extends CombinationGenerator {
  int n;
  int k;
  private int[] state;

  /**
   * TODO.
   * 
   * @param n
   *          TODO
   * @param k
   *          TODO
   * @throws ScboException
   *           TODO
   */
  public RepetitionCombinationGenerator(int n, int k) throws ScboException {
    super(n + k - 1, k - 1);
    state = new int[k];
    this.n = n;
    this.k = k;
  }

  /**
   * TODO.
   */
  public int[] get() {
    int[] state = super.get();
    // System.err.println(Arrays.toString(state));
    this.state[0] = state[0];
    for (int i = 1; i < k - 1; i++) {
      this.state[i] = state[i] - state[i - 1] - 1;
    }
    this.state[k - 1] = n - state[k - 2] + 1;
    return this.state;
  }
}
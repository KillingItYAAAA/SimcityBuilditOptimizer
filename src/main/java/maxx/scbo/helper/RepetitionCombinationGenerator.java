/**
 * 
 */
package maxx.scbo.helper;

import java.util.Arrays;

/**
 * @author phorvath
 *
 */
public class RepetitionCombinationGenerator extends CombinationGenerator {
  int n, k;
  private int[] state;

  public RepetitionCombinationGenerator(int n, int k) throws ScboException {
    super(n + k - 1, k - 1);
    state = new int[k];
    this.n = n;
    this.k = k;
  }

  public int[] get() {
    int[] state = super.get();
    //System.err.println(Arrays.toString(state));
    this.state[0] = state[0];
    for (int i = 1; i < k - 1; i++) {
      this.state[i] = state[i] - state[i - 1] - 1;
    }
    this.state[k - 1] = n - state[k - 2] + 1;
    return this.state;
  }
}
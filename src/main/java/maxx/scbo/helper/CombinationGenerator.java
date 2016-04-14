package maxx.scbo.helper;

public class CombinationGenerator implements Generator {
  private int n;
  private int k;
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
  public CombinationGenerator(int n, int k) throws ScboException {
    if (k > n || n < 1 || k < 1) {
      throw new ScboException();
    }
    this.n = n;
    this.k = k;
    state = new int[k];
    reset();
  }

  /**
   * TODO.
   */
  public boolean step() {
    int idx;
    for (idx = k - 1; idx >= 0 && state[idx] == n - k + idx; idx--) {
    }
    if (idx == -1) {
      return false;
    }
    state[idx]++;
    for (idx++; idx < k; idx++) {
      state[idx] = state[idx - 1] + 1;
    }
    return true;
  }

  public int[] get() {
    return state;
  }

  /**
   * TODO.
   */
  public void reset() {
    for (int i = 0; i < k; i++) {
      state[i] = i;
    }
  }
}
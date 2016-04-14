package maxx.scbo.helper;

public class CombinationGenerator implements Generator {
  private int n, k;
  private int[] state;

  public CombinationGenerator(int n, int k) throws ScboException {
    if (k > n || n < 1 || k < 1) {
      throw new ScboException();
    }
    this.n = n;
    this.k = k;
    state = new int[k];
    reset();
  }

  public boolean step() {
    int i;
    for (i = k - 1; i >= 0 && state[i] == n - k + i; i--)
      ;
    if (i == -1) {
      return false;
    }
    state[i]++;
    for (i++; i < k; i++) {
      state[i] = state[i - 1] + 1;
    }
    return true;
  }

  public int[] get() {
    return state;
  }
  
  public void reset() {
    for (int i = 0; i < k; i++) {
      state[i] = i;
    }
  }
}
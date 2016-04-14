package maxx.scbo.helper;

public class CombinationGenerator implements Generator {
  private int itemno;
  private int partno;
  private int[] state;

  /**
   * TODO.
   * 
   * @param itemno
   *          TODO
   * @param partno
   *          TODO
   * @throws ScboException
   *           TODO
   */
  public CombinationGenerator(int itemno, int partno) throws ScboException {
    if (partno > itemno || itemno < 1 || partno < 1) {
      throw new ScboException();
    }
    this.itemno = itemno;
    this.partno = partno;
    state = new int[partno];
    reset();
  }

  /**
   * TODO.
   */
  public boolean step() {
    int idx;
    for (idx = partno - 1; idx >= 0 && state[idx] == itemno - partno + idx; idx--) {
    }
    if (idx == -1) {
      return false;
    }
    state[idx]++;
    for (idx++; idx < partno; idx++) {
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
    for (int i = 0; i < partno; i++) {
      state[i] = i;
    }
  }
}
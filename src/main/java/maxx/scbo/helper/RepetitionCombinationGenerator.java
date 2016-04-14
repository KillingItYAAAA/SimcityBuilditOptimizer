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
  int itemno;
  int partno;
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
  public RepetitionCombinationGenerator(int itemno, int partno) throws ScboException {
    super(itemno + partno - 1, partno - 1);
    state = new int[partno];
    this.itemno = itemno;
    this.partno = partno;
  }

  /**
   * TODO.
   */
  public int[] get() {
    int[] state = super.get();
    // System.err.println(Arrays.toString(state));
    this.state[0] = state[0];
    for (int i = 1; i < partno - 1; i++) {
      this.state[i] = state[i] - state[i - 1] - 1;
    }
    this.state[partno - 1] = itemno - state[partno - 2] + 1;
    return this.state;
  }
}
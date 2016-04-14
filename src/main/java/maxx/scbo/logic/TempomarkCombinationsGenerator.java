package maxx.scbo.logic;

import maxx.scbo.helper.ProxyGenerator;
import maxx.scbo.helper.RepetitionCombinationGenerator;
import maxx.scbo.helper.ScboException;
import maxx.scbo.helper.VectorGenerator;

public class TempomarkCombinationsGenerator extends ProxyGenerator {
  /**
   * TODO.
   * 
   * @param scenario
   *          TODO
   * @throws ScboException
   *           TODO
   */
  public TempomarkCombinationsGenerator(Scenario scenario) throws ScboException {
    int tempomarkNo = scenario.getTempomarkNo();
    ProxyGenerator[] rcgs = new ProxyGenerator[tempomarkNo];
    int storeNo = scenario.getStoreNo();
    // FIXME: from tempomark types without instances, the actual generator
    // should be leaved out!
    for (int i = 0; i < tempomarkNo; i++) {
      int curTempomarkNo = scenario.getTempomarkByIdx(i).getNoPerDay();
      RepetitionCombinationGenerator rcg = new RepetitionCombinationGenerator(curTempomarkNo,
          storeNo);
      rcgs[i].setGenerator(rcg);
    }
    VectorGenerator vectorGenerator = new VectorGenerator(rcgs);
    setGenerator(vectorGenerator);
  }
}
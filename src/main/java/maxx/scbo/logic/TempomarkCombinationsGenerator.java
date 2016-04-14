package maxx.scbo.logic;

import maxx.scbo.helper.ProxyGenerator;
import maxx.scbo.helper.RepetitionCombinationGenerator;
import maxx.scbo.helper.ScboException;
import maxx.scbo.helper.VectorGenerator;

public class TempomarkCombinationsGenerator extends ProxyGenerator {
  public TempomarkCombinationsGenerator(Scenario scenario) throws ScboException {
    int tempomarkNo = scenario.getTempomarkNo();
    ProxyGenerator[] rcgs = new ProxyGenerator[tempomarkNo];
    int storeNo = scenario.getStoreNo();
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
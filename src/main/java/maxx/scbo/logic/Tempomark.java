package maxx.scbo.logic;

import maxx.scbo.logic.scenario.Scenario;

public class Tempomark {
  private Scenario scenario;
  private String name;
  private int multiplier;
  private int noPerDay;
  
  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public String getName() {
    return name;
  }

  public int getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(int multiplier) {
    this.multiplier = multiplier;
  }

  public int getNoPerDay() {
    return noPerDay;
  }

  public void setNoPerDay(int noPerDay) {
    this.noPerDay = noPerDay;
  }

  /**
   * TODO.
   * 
   * @param scenario TODO
   * @param name TODO
   */
  public Tempomark(Scenario scenario, String name) {
    this.name = name;
    this.scenario = scenario;
    scenario.addTempomark(this);
  }
}
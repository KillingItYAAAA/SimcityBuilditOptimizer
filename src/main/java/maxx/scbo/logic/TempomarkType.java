package maxx.scbo.logic;

import maxx.scbo.logic.config.Configuration;

public class TempomarkType {
  private Configuration configuration;
  private String name;
  private int multiplier;
  private int noPerDay;
  
  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
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
  public TempomarkType(Configuration configuration, String name) {
    this.name = name;
    this.configuration = configuration;
    configuration.addTempomarkType(this);
  }
}
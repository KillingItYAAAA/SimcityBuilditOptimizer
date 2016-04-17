/**
 * 
 */
package maxx.scbo.logic.config;

/**
 * @author phorvath
 *
 */
public class ConfigStore extends ConfigProducer {
  private String name;
  public ConfigStore(Configuration configuration, String name) {
    super(configuration);
    this.name = name;
  }
}
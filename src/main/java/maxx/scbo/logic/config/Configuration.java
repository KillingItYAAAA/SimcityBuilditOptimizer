/**
 * 
 */
package maxx.scbo.logic.config;

import maxx.scbo.helper.Checkable;
import maxx.scbo.helper.IdFactory;
import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.ConstraintSource;
import maxx.scbo.logic.Factory;
import maxx.scbo.logic.Store;
import maxx.scbo.logic.Tempomark;
import maxx.scbo.logic.scenario.Resource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author phorvath
 *
 */
public class Configuration implements Checkable {
  private TreeMap<String, ConfigResource> configResourcesByName = new TreeMap<String, ConfigResource>();
  private TreeMap<Integer, ConfigResource> configResourcesByIdx = new TreeMap<Integer, ConfigResource>();
  private LinkedList<ConfigProducer> configProducers = new LinkedList<ConfigProducer>();
  private TreeSet<ConstraintSource> constraintSources = new TreeSet<ConstraintSource>();
  private Factory factory;
  private ArrayList<Tempomark> tempomarks = new ArrayList<Tempomark>();
  private TreeMap<String, Tempomark> tempomarksByName = new TreeMap<String, Tempomark>();

  private IdFactory resourceIdFactory = new IdFactory();

  public IdFactory getResourceIdFactory() {
    return resourceIdFactory;
  }

  public void addConfigProducer(ConfigProducer configProducer) throws ScboException {
    configProducers.add(configProducer);
  }

  public boolean hasConfigProducer(ConfigProducer configProducer) {
    return configProducers.contains(configProducer);
  }

  public ConfigResource getConfigResourceByName(String name) {
    assert configResourcesByName.containsKey(name);
    return configResourcesByName.get(name);
  }
  
  /* (non-Javadoc)
   * @see maxx.scbo.helper.Checkable#checkValid()
   */
  @Override
  public void checkValid() throws ScboException {
    // TODO Auto-generated method stub
    // FIXME
  }

}
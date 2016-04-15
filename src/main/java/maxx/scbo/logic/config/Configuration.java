/**
 * 
 */
package maxx.scbo.logic.config;

import maxx.scbo.helper.Checkable;
import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.ConstraintSource;
import maxx.scbo.logic.Factory;
import maxx.scbo.logic.Producer;
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
  private TreeMap<String, Store> stores = new TreeMap<String, Store>();
  private TreeMap<String, Resource> resources = new TreeMap<String, Resource>();
  private TreeMap<Integer, Resource> resourcesByIdx = new TreeMap<Integer, Resource>();
  private LinkedList<Producer> producers = new LinkedList<Producer>();
  private TreeSet<ConstraintSource> constraintSources = new TreeSet<ConstraintSource>();
  private Factory factory;
  private ArrayList<Tempomark> tempomarks = new ArrayList<Tempomark>();
  private TreeMap<String, Tempomark> tempomarksByName = new TreeMap<String, Tempomark>();

  /* (non-Javadoc)
   * @see maxx.scbo.helper.Checkable#checkValid()
   */
  @Override
  public void checkValid() throws ScboException {
    // TODO Auto-generated method stub

  }

}
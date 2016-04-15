package maxx.scbo.logic.config;

import maxx.scbo.clui.App;
import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.FactoryResource;
import maxx.scbo.logic.Resource;
import maxx.scbo.logic.ResourceType;
import maxx.scbo.logic.Store;
import maxx.scbo.logic.StoreResource;
import maxx.scbo.logic.Tempomark;
import maxx.scbo.logic.scenario.Scenario;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ConfigLoader extends DefaultHandler {
  private Scenario scenario;
  
  private TreeSet<RawRelation> rawRelations = new TreeSet<RawRelation>();

  private Resource resource;

  public ConfigLoader(Scenario scenario) {
    this.scenario = scenario;
  }

  @Override
  public void startElement(String uri, String localName, String qname, Attributes attributes)
      throws SAXException {
    try {
      if (qname.equalsIgnoreCase("resources")) {
        //
      } else if (qname.equalsIgnoreCase("store")) {
        Store store = new Store(scenario, attributes.getValue("name"));
        scenario.addProducer(store);
      } else if (qname.equalsIgnoreCase("resource")) {
        if (attributes.getValue("type").equalsIgnoreCase("factory")) {
          resource = new FactoryResource(scenario);
        } else if (attributes.getValue("type").equalsIgnoreCase("store")) {
          resource = new StoreResource(scenario);
          ((StoreResource) resource)
              .setStore(scenario.getStoreByName(attributes.getValue("store")));
        } else {
          throw new SAXException("2"); // FIXME
        }

        resource.setName(attributes.getValue("name"));
        resource.setLevel(Integer.parseInt(attributes.getValue("level")));
        resource.setTime(Double.parseDouble(attributes.getValue("time")));
        resource.setValue(Double.parseDouble(attributes.getValue("value")));

      } else if (qname.equalsIgnoreCase("raw")) {
        if (resource == null || !resource.getType().equals(ResourceType.STORE)) {
          throw new SAXException("3"); // FIXME
        }
        String name = attributes.getValue("resource");
        StoreResource storeResource = (StoreResource) resource;
        int rawNo = Integer.parseInt(attributes.getValue("number"));
        RawRelation rawRelation = new RawRelation(storeResource.getName(), name, rawNo);
        rawRelations.add(rawRelation);
      } else if (qname.equalsIgnoreCase("tempomark")) {
        String name = attributes.getValue("name");
        int multiplier = Integer.parseInt(attributes.getValue("multiplier"));
        Tempomark tempomark = new Tempomark(scenario, name);
        tempomark.setMultiplier(multiplier);
      } else {
        throw new ScboException("rules.xml has bad entity: " + qname);
      }
    } catch (ScboException exception) {
      throw new SAXException("1"); // FIXME
    }
  }

  @Override
  public void endElement(String uri, String localName, String qname) throws SAXException {
    if (qname.equalsIgnoreCase("resource")) {
      resource = null;
    }
  }

  @Override
  public void endDocument() throws SAXException {
    for (RawRelation rawRelation : rawRelations) {
      try {
        StoreResource parent = (StoreResource) (scenario.getResource(rawRelation.getParent()));
        Resource child = scenario.getResource(rawRelation.getChild());
        int no = rawRelation.getNo();
        parent.addRaw(child, no);
      } catch (ScboException exception) {
        throw new RuntimeException(exception);
      }
    }
  }

  /**
   * TODO.
   * 
   * @throws ScboException
   *           In case of core logic exception, including config file loading
   *           and parsing.
   */
  public void loadInto() throws ScboException {
    ClassLoader classLoader = App.class.getClassLoader();
    try {
      InputStream rulesStream = classLoader.getResourceAsStream("rules.xml");
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser;
      saxParser = factory.newSAXParser();
      saxParser.parse(rulesStream, this);
    } catch (SAXException | ParserConfigurationException | IOException exception) {
      throw new ScboException(exception);
    }
  }
}
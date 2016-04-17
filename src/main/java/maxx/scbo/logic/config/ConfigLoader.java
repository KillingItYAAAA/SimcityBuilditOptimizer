package maxx.scbo.logic.config;

import maxx.scbo.clui.App;
import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.ResourceType;
import maxx.scbo.logic.Tempomark;
import maxx.scbo.logic.scenario.FactoryResource;
import maxx.scbo.logic.scenario.Resource;
import maxx.scbo.logic.scenario.StoreResource;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ConfigLoader extends DefaultHandler {
  private Configuration configuration;

  private LinkedList<RawRelation> rawRelations = new LinkedList<RawRelation>();

  private Resource resource;

  public ConfigLoader(Configuration configuration) {
    this.configuration = configuration;
  }

  private void elementStore(Attributes attributes) {
    new ConfigStore(configuration, attributes.getValue("name"));
  }

  private void elementResource(Attributes attributes) {
    if (attributes.getValue("type").equalsIgnoreCase("factory")) {
      resource = new FactoryResource(configuration);
    } else if (attributes.getValue("type").equalsIgnoreCase("store")) {
      resource = new StoreResource(scenario);
      ((StoreResource) resource).setStore(scenario.getStoreByName(attributes.getValue("store")));
    } else {
      throw new SAXException("2"); // FIXME
    }

    resource.setName(attributes.getValue("name"));
    resource.setLevel(Integer.parseInt(attributes.getValue("level")));
    resource.setTime(Double.parseDouble(attributes.getValue("time")));
    resource.setValue(Double.parseDouble(attributes.getValue("value")));

  }

  private void elementRaw(Attributes attributes) {
    assert resource != null;
    assert !resource.getType().equals(ResourceType.STORE);

    String name = attributes.getValue("resource");
    StoreResource storeResource = (StoreResource) resource;
    int rawNo = Integer.parseInt(attributes.getValue("number"));

    RawRelation rawRelation = new RawRelation(storeResource.getName(), name, rawNo);
    rawRelations.add(rawRelation);
  }

  private void elementTempomark(Attributes attributes) {
    String name = attributes.getValue("name");
    int multiplier = Integer.parseInt(attributes.getValue("multiplier"));
    Tempomark tempomark = new Tempomark(scenario, name);
    tempomark.setMultiplier(multiplier);
  }

  private void elementImprovement(Attributes attributes) {
    int level = Integer.parseInt(attributes.getValue("level"));
    double acceleration = Double.parseDouble(attributes.getValue("acceleration"));
    configuration.addStoreImprovement(level, acceleration);
  }
  
  @Override
  public void startElement(String uri, String localName, String qname, Attributes attributes)
      throws SAXException {
    switch (qname) {
    case "configuration":
      break;
    case "store":
      elementStore(attributes);
      break;
    case "resource":
      elementResource(attributes);
      break;
    case "raw":
      elementRaw(attributes);
      break;
    case "tempomark":
      elementTempomark(attributes);
      break;
    case "improvement":
      elementImprovement(attributes);
      break;
    default:
      throw new IllegalArgumentException();
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
package maxx.scbo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ConfigLoader {
  private class ConfigHandler extends DefaultHandler {
    private Scenario scenario;
    private Resource resource;

    ConfigHandler(Scenario scenario) {
      this.scenario = scenario;
    }

    @Override
    public void startElement(String uri, String localName, String qname, Attributes attributes)
        throws SAXException {
      if (qname.equalsIgnoreCase("store")) {
        try {
          Store store = new Store(scenario, attributes.getValue("name"));
          scenario.addProducer(store);
        } catch (ScboException exception) {
          throw new SAXException(); // FIXME
        }
      } else if (qname.equalsIgnoreCase("resource")) {
        Resource resource;
        if (attributes.getValue("type").equalsIgnoreCase("factory")) {
          resource = new FactoryResource(scenario);
        } else if (attributes.getValue("type").equalsIgnoreCase("store")) {
          resource = new StoreResource(scenario);
          ((StoreResource) resource)
              .setStore(scenario.getStoreByName(attributes.getValue("store")));
        } else {
          throw new SAXException(); // FIXME
        }
        resource.setLevel(Integer.parseInt(attributes.getValue("level")));
        resource.setName(attributes.getValue("name"));
        resource.setTime(Double.parseDouble(attributes.getValue("time")));
        resource.setValue(Double.parseDouble(attributes.getValue("value")));
      } else if (qname.equalsIgnoreCase("raw")) {
        if (resource == null || resource.getType() != ResourceType.STORE) {
          throw new SAXException(); // FIXME
        }
        StoreResource storeResource = (StoreResource) resource;
        Resource rawResource = scenario.getResource(attributes.getValue("name"));
        try {
          storeResource.addRaw(rawResource, Integer.parseInt(attributes.getValue("number")));
        } catch (ScboException exception) {
          throw new SAXException(); // FIXME
        }
      }
      throw new SAXException("rules.xml has bad entity: " + qname);
    }

    @Override
    public void endElement(String uri, String localName, String qname) throws SAXException {
      resource = null;
    }
  }

  /**
   * TODO.
   * 
   * @param scenario
   *          Th scenario to load the things into.
   * @throws ScboException
   *           In case of core logic exception, including config file loading
   *           and parsing.
   */
  public void loadInto(Scenario scenario) throws ScboException {
    ClassLoader classLoader = App.class.getClassLoader();
    File file = new File(classLoader.getResource("rules.xml").getFile());
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser;
    try {
      saxParser = factory.newSAXParser();
      ConfigHandler configHandler = new ConfigHandler(scenario);
      saxParser.parse(file, configHandler);
    } catch (SAXException | ParserConfigurationException | IOException exception) {
      throw new ScboException("rules.xml resource invalid");
    }
  }
}
package maxx.scbo;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigLoader {
  private class ConfigHandler extends DefaultHandler {
    private Scenario scenario;
    private Resource resource;

    ConfigHandler(Scenario scenario) {
      this.scenario = scenario;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      if (qName.equalsIgnoreCase("store")) {
        Store store = new Store(attributes.getValue("name"));
        try {
          scenario.addStore(store);
        } catch (SCBOException e) {
          throw new SAXException(); // FIXME
        }
      } else if (qName.equalsIgnoreCase("resource")) {
        Resource resource;
        if (attributes.getValue("type").equalsIgnoreCase("factory")) {
          resource = new FactoryResource(scenario);
        } else if (attributes.getValue("type").equalsIgnoreCase("store")) {
          resource = new StoreResource(scenario);
          ((StoreResource) resource).setStore(scenario.getStore(attributes.getValue("store")));
        } else
          throw new SAXException(); // FIXME
        resource.setLevel(Integer.parseInt(attributes.getValue("level")));
        resource.setName(attributes.getValue("name"));
        resource.setTime(Double.parseDouble(attributes.getValue("time")));
        resource.setValue(Double.parseDouble(attributes.getValue("value")));
      } else if (qName.equalsIgnoreCase("raw")) {
        if (resource == null || resource.getType() != ResourceType.STORE)
          throw new SAXException(); // FIXME
        StoreResource storeResource = (StoreResource) resource;
        Resource rawResource = scenario.getResource(attributes.getValue("name"));
        try {
          storeResource.addRaw(rawResource, Integer.parseInt(attributes.getValue("number")));
        } catch (SCBOException e) {
          throw new SAXException(); // FIXME
        }
      }
      throw new SAXException("rules.xml has bad entity: " + qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      resource = null;
    }
  }

  public void loadInto(Scenario scenario) throws SCBOException {
    ClassLoader classLoader = App.class.getClassLoader();
    File file = new File(classLoader.getResource("rules.xml").getFile());
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser;
    try {
      saxParser = factory.newSAXParser();
    } catch (SAXException | ParserConfigurationException e) {
      throw new SCBOException("rules.xml resource invalid");
    }
    ConfigHandler configHandler = new ConfigHandler(scenario);
  }
}
package maxx.scbo.logic.loader;

import maxx.scbo.clui.App;
import maxx.scbo.helper.ScboException;
import maxx.scbo.logic.ResourceType;
import maxx.scbo.logic.TempomarkType;
import maxx.scbo.logic.config.ConfigProducer;
import maxx.scbo.logic.config.ConfigResource;
import maxx.scbo.logic.config.Configuration;
import maxx.scbo.logic.config.RawRelation;
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
  Configuration configuration;

  LinkedList<RawRelation> rawRelations = new LinkedList<RawRelation>();

  ConfigResource configResource;
  ConfigProducer configProducer;

  public ConfigLoader(Configuration configuration) {
    this.configuration = configuration;
  }

  private void elementTempomark(Attributes attributes) {
    String name = attributes.getValue("name");
    int multiplier = Integer.parseInt(attributes.getValue("multiplier"));
    TempomarkType tempomarkType = new TempomarkType(scenario, name);
    tempomarkType.setMultiplier(multiplier);
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
    case "producer":
      elementProducerStart(attributes);
      break;
    case "resource":
      elementResourceStart(attributes);
      break;
    case "raw":
      elementRawStart(attributes);
      break;
    case "tempomark":
      elementTempomarkStart(attributes);
      break;
    case "improvement":
      elementImprovementStart(attributes);
      break;
    default:
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qname) throws SAXException {
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
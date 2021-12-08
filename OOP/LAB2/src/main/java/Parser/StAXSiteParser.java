package Parser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StAXSiteParser extends XMLParser{
    @Override
    public void parse(File xmlFile) {
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlFile));
            while (xmlEventReader.hasNext()) {
                XMLEvent nextEvent = xmlEventReader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    nextEvent = xmlEventReader.nextEvent();
                    if (nextEvent.isCharacters()) {
                        this.siteHandler.setElementValue(nextEvent.asCharacters().getData());
                        this.siteHandler.setField(startElement.getName().getLocalPart());
                    }
                }
            }
            XMLCreator.createXML(this.siteHandler.getInternetPages(), "src/main/resources/StAX.xml");
        } catch (FileNotFoundException | XMLStreamException ignore) {
        }
    }
}

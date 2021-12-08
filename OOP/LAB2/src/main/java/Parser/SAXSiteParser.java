package Parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import Site.*;

public class SAXSiteParser extends XMLParser {
    @Override
    public void parse(File xmlFile) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SAXSiteHandler saxSiteHandler = new SAXSiteHandler();
            saxParser.parse(xmlFile, saxSiteHandler);
            this.siteHandler.getInternetPages().setSiteList(saxSiteHandler.getInternetPages().getSiteList());

            XMLCreator.createXML(this.siteHandler.getInternetPages(), "src/main/resources/SAX.xml");
        } catch (ParserConfigurationException | SAXException | IOException ignore) {
        }

    }
}

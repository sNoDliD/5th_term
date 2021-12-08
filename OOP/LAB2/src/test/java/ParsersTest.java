import Parser.DOMSiteParser;
import Parser.SAXSiteParser;
import Parser.StAXSiteParser;
import Parser.XMLValidator;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ParsersTest {
    private final DOMSiteParser domSiteParser = new DOMSiteParser();
    private final SAXSiteParser saxSiteParser = new SAXSiteParser();
    private final StAXSiteParser staxSiteParser = new StAXSiteParser();
    private final String xmlFilePath = "src/main/resources/pages.xml";
    private final String xsdFilePath = "src/main/resources/pages.xsd";

    @Test
    public void DOMParserTest() {
        Assert.assertTrue(XMLValidator.isValid("src/main/resources/DOM.xml", xsdFilePath));
        domSiteParser.parse(new File(xmlFilePath));
    }

    @Test
    public void SAXParserTest() {
        Assert.assertTrue(XMLValidator.isValid("src/main/resources/SAX.xml", xsdFilePath));
        saxSiteParser.parse(new File(xmlFilePath));
    }

    @Test
    public void StAXParserTest() {
        Assert.assertTrue(XMLValidator.isValid("src/main/resources/StAX.xml", xsdFilePath));
        staxSiteParser.parse(new File(xmlFilePath));
    }
}

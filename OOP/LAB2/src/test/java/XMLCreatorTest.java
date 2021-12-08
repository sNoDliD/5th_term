import Parser.DOMSiteParser;
import Parser.XMLCreator;
import Parser.XMLValidator;
import Site.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLCreatorTest {
    @Test
    public void creatorTest() {
        InternetPages internetPages = new InternetPages();
        List<Site> pages = new ArrayList<>();
        Site site = new Site();
        site.setChars(new Chars());
        site.setTitle("Site");
        site.setSiteType(SiteType.ADVERTISING);
        site.setAuthorize(false);
        site.getChars().setEmail("email@gmail.com");
        site.getChars().setPoll(true);
        site.getChars().setPollType(PollType.ANONYMOUS);
        site.getChars().setFree(false);
        pages.add(site);
        internetPages.setSiteList(pages);
        XMLCreator.createXML(internetPages, "src/test/java/resources/test.xml");
        Assert.assertTrue(XMLValidator.isValid("src/test/java/resources/test.xml", "src/main/resources/pages.xsd"));
        DOMSiteParser domSiteParser = new DOMSiteParser();
        domSiteParser.parse(new File("src/test/java/resources/test.xml"));
        Assert.assertEquals(domSiteParser.outputForTest(), internetPages.output());
    }
}

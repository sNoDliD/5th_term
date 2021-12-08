package Parser;

import Site.*;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.Objects;

public class SiteHandler extends DefaultHandler {
    private static final String SITE = "site";
    private static final String TITLE = "title";
    private static final String SITETYPE = "sitetype";
    private static final String AUTHORIZE = "authorize";
    private static final String CHARS = "chars";
    private static final String EMAIL = "email";
    private static final String POLL = "poll";
    private static final String POLLTYPE = "polltype";
    private static final String FREE = "free";

    private InternetPages internetPages = new InternetPages();
    private String elementValue;

    public void setElementValue(String elementValue) {
        this.elementValue = elementValue;
    }

    public String getName() {
        return "site";
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        elementValue = new String(ch, start, length);
    }

    @Override
    public void startDocument() {
        internetPages = new InternetPages();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) {
        switch (qName) {
            case SITE:
                Site site = new Site();
                internetPages.getSiteList().add(site);
                break;
            case CHARS:
                lastSite().setChars(new Chars());
                break;
        }
    }

    @Override
    public void endElement(String uri, String lName, String qName) {
        setField(qName);
    }

    private Site lastSite() {
        List<Site> temp = internetPages.getSiteList();
        return temp.get(temp.size() - 1);
    }

    public InternetPages getInternetPages() {
        return internetPages;
    }

    public void setField(String qName) {
        switch (qName) {
            default:
                break;
            case SITE:
                Site site = new Site();
                internetPages.getSiteList().add(site);
                break;
            case CHARS:
                lastSite().setChars(new Chars());
                break;
            case TITLE:
                lastSite().setTitle(elementValue);
                break;
            case SITETYPE:
                lastSite().setSiteType(SiteType.valueOf(elementValue));
                break;
            case AUTHORIZE:
                lastSite().setAuthorize(Boolean.parseBoolean(elementValue));
                break;
            case EMAIL:
                lastSite().getChars().setEmail(elementValue);
                break;
            case POLL:
                lastSite().getChars().setPoll(Boolean.parseBoolean(elementValue));
                break;
            case POLLTYPE:
                if (Objects.equals(elementValue, "AUTHORIZED") || Objects.equals(elementValue, "ANONYMOUS")) {
                    lastSite().getChars().setPollType(PollType.valueOf(elementValue));
                }
                break;
            case FREE:
                lastSite().getChars().setFree(Boolean.parseBoolean(elementValue));
                break;
        }
    }
}

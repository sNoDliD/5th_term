package Parser;

import Site.InternetPages;

import java.io.File;

public abstract class XMLParser {
    InternetPages internetPages = new InternetPages();
    SiteHandler siteHandler = new SiteHandler();

    public abstract void parse(File xmlFile);
}

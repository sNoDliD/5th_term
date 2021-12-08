package Parser;

import Site.InternetPages;
import Site.Site;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XMLCreator {
    public static void createXML(InternetPages internetPages, String xmlFilePath) {
        try {
            List<Site> siteList = internetPages.getSiteList();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("InternetPages");
            document.appendChild(root);
            for (Site site : siteList) {
                Element siteElement = document.createElement("site");
                root.appendChild(siteElement);

                Element title = document.createElement("title");
                title.appendChild(document.createTextNode(site.getTitle()));
                siteElement.appendChild(title);

                Element siteType = document.createElement("sitetype");
                siteType.appendChild(document.createTextNode(String.valueOf(site.getSiteType())));
                siteElement.appendChild(siteType);

                Element authorize = document.createElement("authorize");
                authorize.appendChild(document.createTextNode(String.valueOf(site.isAuthorize())));
                siteElement.appendChild(authorize);

                Element chars = document.createElement("chars");
                siteElement.appendChild(chars);

                Element email = document.createElement("email");
                email.appendChild(document.createTextNode(site.getChars().getEmail()));
                chars.appendChild(email);

                Element poll = document.createElement("poll");
                poll.appendChild(document.createTextNode(String.valueOf(site.getChars().isPoll())));
                chars.appendChild(poll);

                Element pollType = document.createElement("polltype");
                pollType.appendChild(document.createTextNode(String.valueOf(site.getChars().getPollType())));
                chars.appendChild(pollType);

                Element free = document.createElement("free");
                free.appendChild(document.createTextNode(String.valueOf(site.getChars().isFree())));
                chars.appendChild(free);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException ignore) {
        }
    }
}

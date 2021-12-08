package Parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DOMSiteParser extends XMLParser {
    private static String getChildValue(Element element, String name) {
        Element child = (Element) element.getElementsByTagName(name).item(0);
        if (child == null) {
            return "";
        }
        Node node = child.getFirstChild();
        if (node == null) {
            return "";
        } else {
            return node.getNodeValue();
        }
    }

    @Override
    public void parse(File xmlFile) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName(siteHandler.getName());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    siteHandler.setField(element.getNodeName());
                    NodeList childNodesList = element.getChildNodes();
                    for (int j = 0; j < childNodesList.getLength(); j++) {
                        if (childNodesList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element child = (Element) childNodesList.item(j);
                            siteHandler.setElementValue(getChildValue(element, child.getNodeName()));
                            siteHandler.setField(child.getNodeName());
                            NodeList childOfChildNodesList = child.getChildNodes();
                            for (int k = 0; k < childOfChildNodesList.getLength(); k++) {
                                if (childOfChildNodesList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                    Element childOfChild = (Element) childOfChildNodesList.item(k);
                                    siteHandler.setElementValue(getChildValue(child, childOfChild.getNodeName()));
                                    siteHandler.setField(childOfChild.getNodeName());
                                }
                            }
                        }
                    }
                }
            }

            XMLCreator.createXML(this.siteHandler.getInternetPages(), "src/main/resources/DOM.xml");
        } catch (ParserConfigurationException | IOException | SAXException ignore) {
        }
    }

    public String outputForTest() {
        return this.siteHandler.getInternetPages().output();
    }
}

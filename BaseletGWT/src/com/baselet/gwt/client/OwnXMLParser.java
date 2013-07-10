package com.baselet.gwt.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.element.ElementFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.umlet.element.experimental.ElementId;

public class OwnXMLParser {

	private static final Logger log = Logger.getLogger(OwnXMLParser.class);

	private static final String ELEMENT = "element";
	private static final String ZOOM_LEVEL = "zoom_level";
	private static final String ID = "id";
	private static final String COORDINATES = "coordinates";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String W = "w";
	private static final String H = "h";
	private static final String PANEL_ATTRIBUTES = "panel_attributes";
	private static final String ADDITIONAL_ATTRIBUTES = "additional_attributes";

	public static List<GridElement> xmlToGridElements(String xml, Selector selector) {
		List<GridElement> returnList = new ArrayList<GridElement>();
		try {
			// parse the XML document into a DOM
			Document messageDom = XMLParser.parse(xml);

			NodeList elements = messageDom.getElementsByTagName(ELEMENT);
			for (int i = 0; i < elements.getLength(); i++) {
				Element element = (Element) elements.item(i);
				try {
					ElementId id = ElementId.valueOf(element.getElementsByTagName(ID).item(0).getFirstChild().getNodeValue());
					Element coord = (Element) element.getElementsByTagName(COORDINATES).item(0);
					Rectangle rect = new Rectangle(getInt(coord, X), getInt(coord, Y), getInt(coord, W), getInt(coord, H));
					String panelAttributes = element.getElementsByTagName(PANEL_ATTRIBUTES).item(0).getFirstChild().getNodeValue();

					String additionalPanelAttributes = "";
					Node additionalAttrNode = element.getElementsByTagName(ADDITIONAL_ATTRIBUTES).item(0);
					if (additionalAttrNode != null && additionalAttrNode.getFirstChild() != null) {
						additionalPanelAttributes = additionalAttrNode.getFirstChild().getNodeValue();
					}
					returnList.add(ElementFactory.create(id, rect, panelAttributes, additionalPanelAttributes, selector));
				} catch (Exception e) {
					log.error("Element has invalid XML structure: " + element, e);
				}
			}
		} catch (DOMException e) {
			log.error("Parsing error", e);
			Window.alert("Could not parse XML document.");
		}
		return returnList;
	}

	private static Integer getInt(Element coordinates, String tag) {
		return Integer.valueOf(coordinates.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue());
	}

	public static String gridElementsToXml(Collection<GridElement> gridElements) {
		Document doc = XMLParser.createDocument();

		Element zoomElement = doc.createElement(ZOOM_LEVEL);
		zoomElement.appendChild(doc.createTextNode("10"));

		Element diagramElement = doc.createElement("diagram");
		diagramElement.setAttribute("program", Utils.getProgramnameLowerCase());
		diagramElement.setAttribute("version", Utils.getProgramVersion());
		diagramElement.appendChild(zoomElement);
		doc.appendChild(diagramElement);

		for (GridElement ge : gridElements) {
			diagramElement.appendChild(
				create(doc, ELEMENT, 
					create(doc, ID, doc.createTextNode(ge.getId().toString())), 
					create(doc, COORDINATES, 
							create(doc, X, doc.createTextNode(ge.getRectangle().getX()+"")), 
							create(doc, Y, doc.createTextNode(ge.getRectangle().getY()+"")), 
							create(doc, W, doc.createTextNode(ge.getRectangle().getWidth()+"")), 
							create(doc, H, doc.createTextNode(ge.getRectangle().getHeight()+""))), 
					create(doc, PANEL_ATTRIBUTES, doc.createTextNode(ge.getPanelAttributes())), 
					create(doc, ADDITIONAL_ATTRIBUTES, doc.createTextNode(ge.getAdditionalAttributes()))
				));
		}
		return doc.toString();
	}

	private static Element create(Document doc, String name, Node ... children) {
		Element element = doc.createElement(name);
		for (Node c : children) {
			element.appendChild(c);
		}
		return element;
	}

}
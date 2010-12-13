package de.htwk.autolat.tools.XMLParser;


import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLParser {

	public OutputObject parseFile(String fileName) throws IOException, JDOMException {
		Document doc = new SAXBuilder().build(fileName);
		Element root = doc.getRootElement();
		ArrayList<Element> templist = new ArrayList<Element>();
		templist.add(root);
		return buildObject(new OutputObject(), templist);


	}


	public OutputObject parseString(String xmlString) throws IOException, JDOMException {

		// Document doc = new SAXBuilder().build( filename );
		// maybe better like this to be able to pass a string instead of a URI?
		StringReader xmlStringReader = new StringReader(xmlString);
		Document doc = new SAXBuilder().build( xmlStringReader );

		//get root node
		Element root = doc.getRootElement();

		/*
		if(root.getName().equals("start")) {
			return buildObject(new OutputObject(), root.getChildren());
		}
		else {
			return null;
		}
		*/

		// quick (and dirty) workaround to pass a list of just the root-element
		ArrayList<Element> templist = new ArrayList<Element>();
		templist.add(root);
		return buildObject(new OutputObject(), templist);

	}

	private OutputObject buildObject(OutputObject object, List elements) throws MalformedURLException {

		for(int i = 0; i < elements.size(); i++) {
			Element ele = (Element)elements.get(i);
			if(ele.getName().equals("Pre")) {
				object.addPre(new Pre(ele.getText()));
			}
			if(ele.getName().equals("Text")) {
				object.addText(ele.getText());
			}
			if(ele.getName().equals("Link")) {
				object.addLink(new Link( ele.getAttributeValue("href"), ele.getText(), ele.getAttributeValue("alt") ) );
			}
			if(ele.getName().equals("Space")) {
				object.setSpace(new Space( Double.valueOf(ele.getAttributeValue("height")), Double.valueOf(ele.getAttributeValue("width")), ele.getAttributeValue("unit")));
			}
			if(ele.getName().equals("Above")) {
				object.addAbove(new Above(buildObject(new OutputObject(), ele.getChildren())));
			}
			if(ele.getName().equals("Beside")) {
				object.addBeside(new Beside(buildObject(new OutputObject(), ele.getChildren())));
			}
			if(ele.getName().equals("Itemize")) {
				object.addItemize(new Itemize(buildObject(new OutputObject(), ele.getChildren())));
			}
			if(ele.getName().equals("Figure")) {
				Figure fig = buildFigure(ele.getChildren());
				object.addFigure(fig);
			}
		}
		return object;
	}

	private Figure buildFigure(List elements) throws MalformedURLException {
		Figure fig = new Figure();
		for(int i = 0; i < elements.size(); i++) {
			Element ele = (Element)elements.get(i);
			if(ele.getName().equals("Pre")) {
				fig.setDescription(new OutputObject());
				fig.getDescription().addPre(new Pre(ele.getText()));
			}
			if(ele.getName().equals("Text")) {
				fig.setDescription(new OutputObject());
				fig.getDescription().addText(ele.getText());
			}
			if(ele.getName().equals("Link")) {
				fig.setDescription(new OutputObject());
				fig.getDescription().addLink(new Link( ele.getAttributeValue("href"), ele.getText(), ele.getAttributeValue("alt") ) );
			}
			if(ele.getName().equals("Space")) {
				fig.setDescription(new OutputObject());
				fig.getDescription().setSpace(new Space( Double.valueOf(ele.getAttributeValue("height")), Double.valueOf(ele.getAttributeValue("width")), ele.getAttributeValue("unit")));
			}
			if(ele.getName().equals("Above")) {
				fig.setDescription(new OutputObject());
				fig.getDescription().addAbove(new Above(buildObject(new OutputObject(), ele.getChildren())));
			}
			if(ele.getName().equals("Beside")) {
				fig.setDescription(new OutputObject());
				fig.getDescription().addBeside(new Beside(buildObject(new OutputObject(), ele.getChildren())));
			}
			if(ele.getName().equals("Itemize")) {
				fig.setDescription(new OutputObject());
				fig.getDescription().addItemize(new Itemize(buildObject(new OutputObject(), ele.getChildren())));
			}
			if(ele.getName().equals("Image")) {
				fig.setPic(PictureFactory.getInstance().getPicture( new Picture(ele.getAttributeValue("type"),
						ele.getText(),
						ele.getAttributeValue("alt"),
						Double.valueOf(ele.getAttributeValue("width")),
						Double.valueOf(ele.getAttributeValue("height")),
						ele.getAttributeValue("unit"))));
			}
		}
		return fig;
	}

}

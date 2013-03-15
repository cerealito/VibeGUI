package com.airbus.vibe.dalo;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class PlatformFastReader {

	private Document document;
	private NodeList p_nodes;
	
	public PlatformFastReader(String path_s) {
		
		DOMParser parser = new DOMParser();
		
		try {
			parser.parse(path_s);
		} catch (SAXException e1) {
			System.err.println("Error parsing");
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println("Oops, can not read from " + path_s);
		}

		document = parser.getDocument();
		
		document.normalize();
		
		this.p_nodes = document.getElementsByTagName("Platform");
		
	}

	public ArrayList<String> getPlatforms() {
		ArrayList<String> al = new ArrayList<String>();
		
		for (int i=0; i < this.p_nodes.getLength(); i++) {
			try {
				al.add(p_nodes.item(i).
				    getAttributes().getNamedItem("name").getNodeValue());
			}
			catch (NullPointerException e) {
				//probably no attribute "name"
				// do nothing
			}
			
		}
		
		return al;
	}
	
	
}

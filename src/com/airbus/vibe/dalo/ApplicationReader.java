package com.airbus.vibe.dalo;


import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class ApplicationReader {

	private Document document;
	private NodeList lcs_nodes;
	private NodeList syn_nodes;
	
	public ApplicationReader(String path_s) {
		
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
		
		this.lcs_nodes = document.getElementsByTagName("LocalCtrl");
		this.syn_nodes = document.getElementsByTagName("Synoptic");
		
	}
	
	/**
	 * Returns all the actors (either a list of models or a single synoptic)
	 * existing inside a given actor.
	 * @param actor
	 * @return
	 */
	public ActorWrapper[] getActors(String actor) {
		// TODO
		return null;
	}

	
}

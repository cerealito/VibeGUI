package com.airbus.vibe.dalo;


import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class ActorsFileReader {

	private Document document;
	private NodeList lcs_nodes;
	private NodeList syn_nodes;
	
	public ActorsFileReader(String path_s) {
		System.out.println("!!!! §§§§ Creating a new ActorsFileReader");
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
		
		ArrayList<ActorWrapper> actorList = new ArrayList<ActorWrapper>();
		
		for (int i=0; i < this.syn_nodes.getLength(); i++) {
			try {
				
				String n = syn_nodes.item(i).getAttributes().
						getNamedItem("name").getNodeValue();
				
				String v = syn_nodes.item(i).getAttributes().
						getNamedItem("version").getNodeValue();
				ActorWrapper aw = new ActorWrapper(n, v);
				actorList.add(aw);
			}
			catch (NullPointerException e) {
				//probably no attribute "name" or "version"
				// do nothing
			}
			
		}
		
		return (ActorWrapper[])actorList.toArray();
	}

	
}

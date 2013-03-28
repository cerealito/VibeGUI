package com.airbus.vibe.dalo;


import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class ActorsFileReader {

	private Document document;
	
	private NodeList lcs_nodes;
	private NodeList syn_nodes;
	private NodeList pkg_nodes;
	private NodeList cpm_nodes;
	
	public ActorsFileReader(String path_s) {
		System.out.println("Creating new ActorsFileReader at " + this.hashCode());
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
		this.cpm_nodes = document.getElementsByTagName("CPIOM");
		this.pkg_nodes = document.getElementsByTagName("SESAME");
	}
	
	/**
	 * Returns all the actors (either a list of models or a single synoptic)
	 * existing inside a given actor.
	 * @param actor_nw
	 * @return
	 */
	public SimItemWrapper[] getItemsInActor(NodeWrapper actor_nw) {
	
		if (actor_nw.type().equalsIgnoreCase("LC")) {
			// TODO
		}
		else if (actor_nw.type().equalsIgnoreCase("SYNOPTIC")) {
			// TODO
		}
		else if (actor_nw.type().equalsIgnoreCase("CPIOM")) {
			// TODO
		}
		else if (actor_nw.type().equalsIgnoreCase("SESAME")) {
			// TODO
		}
		
		return null;
	}
	
	
	/**
	 * Returns all the actors (either a list of models or a single synoptic)
	 * existing inside a given actor.
	 * @param actor
	 * @return
	 */
	public SimItemWrapper[] getItemsInActor(String actor) {
		
		ArrayList<SimItemWrapper> actorList = new ArrayList<SimItemWrapper>();
		boolean found = false;
		
		// search for the given actor in the LCS
		for (int i=0; i < this.lcs_nodes.getLength(); i++) {
			try {		
				String lc_name = lcs_nodes.item(i).getAttributes().
						getNamedItem("name").getNodeValue();
				
				if (actor.equals(lc_name)) {
					// =============================================
					// this actor is the actor they are looking for 
					// and it is in the LCS.
					// Go one level down and get all contained nodes.
					// =============================================
	
					Node lc_node = lcs_nodes.item(i);
					
					NodeList lc_children = lc_node.getChildNodes();
					
					// sweep and create ActorWrappers if they are tagged "Model"
					for (int j=0; j < lc_children.getLength(); j++) {
						if ( lc_children.item(j).getNodeName().equals("Model") ) {
							try { 
								String n = lc_children.item(j).getAttributes().
										        getNamedItem("name").getNodeValue();

								String v = lc_children.item(j).getAttributes().
										        getNamedItem("version").getNodeValue();
								SimItemWrapper aw = new SimItemWrapper(n, v);
								actorList.add(aw);
							}
							catch (NullPointerException e) {
								//probably no attribute "name" or "version"
								// do nothing
							}	
						}							
					}
					
					// assume every LC has different name:
					// stop looking on the first one found
					found = true;
					break;
				}
			}
			catch (NullPointerException e) {
				//probably no attribute "name"
				// do nothing
			}	
		}
		
		if ( ! found ) {
			// search for the given actor in the synoptics
			for (int i=0; i < this.syn_nodes.getLength(); i++) {
				try {		
					String n = syn_nodes.item(i).getAttributes().
							getNamedItem("name").getNodeValue();
					
					if (actor.equals(n)) {
						String v = syn_nodes.item(i).getAttributes().
						           getNamedItem("version").getNodeValue();
						SimItemWrapper aw = new SimItemWrapper(n, v);
						actorList.add(aw);
						
						// only one synoptic per synoptic actor! => stop
						found = true;
						break;
					}
				}
				catch (NullPointerException e) {
					//probably no attribute "name" or "version"
					// do nothing
				}	
			}
		}	

		return actorList.toArray(new SimItemWrapper[0]);
	}

}

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
	
	/**
	 * Reads an XML file, usually named Applications_XXX.xml
	 * 
	 * @param path_s the full path to the xml file
	 */
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
	 * Returns all the simulation items (Modems/synoptics) of a particular "Actor" 
	 * defined in the "Applications_<APP>.xml" file, given a corresponding NodeWrapper
	 * which contains an "appli" node from the "Platforms_<APP>.xml" file.
	 * 
	 * The confusing terminology comes from the poor syntax of the xml files.
	 * 
	 * @param actor_nw 
	 * @return
	 */
	public SimItemWrapper[] getItemsInActor(NodeWrapper actor_nw) {
	
		if (actor_nw.type().equalsIgnoreCase("LC")) {
			return this.getItemsInLC(actor_nw);
		}
		else if (actor_nw.type().equalsIgnoreCase("SYNOPTIC")) {
			return this.getSynopticInfo(actor_nw);
		}
		else if (actor_nw.type().equalsIgnoreCase("CPIOM")) {
			return this.getCpiomInfo(actor_nw);
		}
		else if (actor_nw.type().equalsIgnoreCase("SESAME")) {
			return this.getSesameInfo(actor_nw);
		}
		
		return null;
	}

	/**
	 * Returns the info of a SESAME
	 * @param sesame_nw the node_wrapper from the "Platforms_XML" file 
	 * @return
	 */
	private SimItemWrapper[] getSesameInfo(NodeWrapper sesame_nw) {
		
		if ( ! sesame_nw.type().equalsIgnoreCase("sesame") ) {
			//the passed argument is not of type cpiom
			System.err.println("passed node wrapper does not contain a node of type SESAME");
			return null;
		}
		
		ArrayList<SimItemWrapper> actorList = new ArrayList<SimItemWrapper>();
		
		// search for the given appli in the SESAME nodes
		for (int i=0; i < this.pkg_nodes.getLength(); i++) {
			try {		
				String n = pkg_nodes.item(i).getAttributes().
						getNamedItem("name").getNodeValue();
				
				if (sesame_nw.actorName().equals(n)) {
					String v = pkg_nodes.item(i).getAttributes().
					           getNamedItem("PackageVersion").getNodeValue();
					SimItemWrapper aw = new SimItemWrapper(n, v);
					actorList.add(aw);
					
					// only one synoptic per synoptic actor! => stop
					break;
				}
			}
			catch (NullPointerException e) {
				//probably no attribute "name" or "version"
				// do nothing
			}	
		}

		return actorList.toArray(new SimItemWrapper[0]);
	}

	
	/**
	 * Returns the info of a CPIOM
	 * @param cpiom_nw the node_wrapper from the "Platforms_XML" file 
	 * @return
	 */
	private SimItemWrapper[] getCpiomInfo(NodeWrapper cpiom_nw) {
		
		if ( ! cpiom_nw.type().equalsIgnoreCase("cpiom") ) {
			//the passed argument is not of type cpiom
			System.err.println("passed node wrapper does not contain a node of type CPIOM");
			return null;
		}
		
		ArrayList<SimItemWrapper> actorList = new ArrayList<SimItemWrapper>();
		
		// search for the given appli in the CPIOM
		for (int i=0; i < this.cpm_nodes.getLength(); i++) {
			try {		
				String n = cpm_nodes.item(i).getAttributes().
						getNamedItem("name").getNodeValue();
				
				if (cpiom_nw.actorName().equals(n)) {
					String v = cpm_nodes.item(i).getAttributes().
					           getNamedItem("version").getNodeValue();
					SimItemWrapper aw = new SimItemWrapper(n, v);
					actorList.add(aw);
					
					// only one synoptic per synoptic actor! => stop
					break;
				}
			}
			catch (NullPointerException e) {
				//probably no attribute "name" or "version"
				// do nothing
			}	
		}

		return actorList.toArray(new SimItemWrapper[0]);
	}

	/**
	 * Returns the models in an LC 
	 * @param lc_nw the node_wrapper from the "Platforms_XML" file 
	 * @return
	 */
	private SimItemWrapper[] getItemsInLC(NodeWrapper lc_nw) {
		
		if ( ! lc_nw.type().equalsIgnoreCase("lc") ) {
			//the passed argument is not an LC
			System.err.println("passed node wrapper does not contain a node of type LC");
			return null;
		}
		
		ArrayList<SimItemWrapper> actorList = new ArrayList<SimItemWrapper>();
		
		// search for the given appli in the LCS
		for (int i=0; i < this.lcs_nodes.getLength(); i++) {
			try {		
				String current_name = lcs_nodes.item(i).getAttributes().
						getNamedItem("name").getNodeValue();
				
				if (lc_nw.actorName().equals(current_name)) {
					// =============================================
					// this actor is the actor they are looking for.
					// Go one level down and get all contained nodes.
					// =============================================
	
					Node interesting_node = lcs_nodes.item(i);
					
					NodeList children = interesting_node.getChildNodes();
					
					// sweep and create ActorWrappers if they are tagged "Model"
					for (int j=0; j < children.getLength(); j++) {
						if ( children.item(j).getNodeName().equals("Model") ) {
							try { 
								String n = children.item(j).getAttributes().
										       getNamedItem("name").getNodeValue();

								String v = children.item(j).getAttributes().
										       getNamedItem("version").getNodeValue();
								SimItemWrapper aw = new SimItemWrapper(n, v);
								actorList.add(aw);
							}
							catch (NullPointerException e) {
								//probably no attribute "name" or "version" on the Model tag
								// do nothing
							}	
						}							
					}

					break;
				}
			}
			catch (NullPointerException e) {
				//probably no attribute "name" on the LC tag
				// do nothing
			}	
		}
		
		return actorList.toArray(new SimItemWrapper[0]);
	}

	
	/**
	 * Returns the sim items from a synoptic (usually just one) 
	 * @param lc_nw the node_wrapper from the "Platforms_XML" file 
	 * @return
	 */	
	private SimItemWrapper[] getSynopticInfo(NodeWrapper syn_nw) {
		
		if ( ! syn_nw.type().equalsIgnoreCase("synoptic") ) {
			//the passed argument is not a Synoptic
			System.err.println("passed node wrapper does not contain a node of type Synoptic");
			return null;
		}
		
		ArrayList<SimItemWrapper> actorList = new ArrayList<SimItemWrapper>();
		
		// search for the given appli in the synoptics
		for (int i=0; i < this.syn_nodes.getLength(); i++) {
			try {		
				String n = syn_nodes.item(i).getAttributes().
						getNamedItem("name").getNodeValue();
				
				if (syn_nw.actorName().equals(n)) {
					String v = syn_nodes.item(i).getAttributes().
					           getNamedItem("version").getNodeValue();
					SimItemWrapper aw = new SimItemWrapper(n, v);
					actorList.add(aw);
					
					// only one synoptic per synoptic actor! => stop
					break;
				}
			}
			catch (NullPointerException e) {
				//probably no attribute "name" or "version"
				// do nothing
			}	
		}

		return actorList.toArray(new SimItemWrapper[0]);
	}
}

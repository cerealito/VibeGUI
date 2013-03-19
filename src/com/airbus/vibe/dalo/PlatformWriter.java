package com.airbus.vibe.dalo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import mx.bitch.util.Tree;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

public class PlatformWriter {

	private Document document;
	private ArrayList<NodeWrapper>    appliList;
	private ArrayList<RestorableNode> backupList;
	
	public  Tree     <NodeWrapper> tree;

	/**
	 * Constructor with a file
	 * @param myFile
	 * @throws IOException if the passed string is not the path of a readable file
	 */
	public PlatformWriter(String path_s, String platform2trim) {
		
		DOMParser parser = new DOMParser();
		this.appliList = new ArrayList<NodeWrapper>();
		this.backupList = new ArrayList<RestorableNode>();
		
		try {
			parser.parse(path_s);
		} catch (SAXException e1) {
			System.err.println("Error parsing");
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println("Oops, can not read from " + path_s);
		}
		
		this.document = parser.getDocument();
		this.document.normalize();
		
		Node        plat_n;
		NodeWrapper plat_nw;

		if ((plat_n = this.getPlatformNode(platform2trim)) == null) {
			System.err.println("Platform " + platform2trim + " is not defined");
			System.err.println("in file " + path_s);
			throw new NullPointerException();
		}
		
		plat_nw = new NodeWrapper(plat_n); 
		this.tree  = new Tree<NodeWrapper>(plat_nw);

		this.populate(this.tree);
	}

	
	/**
	 * recursively adds leaves to the tree of from the DOM element of
	 * the passed branch 
	 * 
	 */
	private void populate(Tree<NodeWrapper> t) {		
		NodeWrapper tnw = t.getElement();
		
		Node currXmlNode = tnw.getXmlNode().getFirstChild();
		// add first level kids
		while(currXmlNode != null) {
			//skip all attributes; just get the ELEMENTS
			if (currXmlNode.getNodeType() == Node.ELEMENT_NODE) {
				try {					
					// create a new NodeWrapper and add it as a son of t;
					NodeWrapper nw = new NodeWrapper(currXmlNode); 
					t.addLeaf(nw);
					
					if (nw.isAppli()) {
						this.appliList.add(nw);				
						System.out.println(">>> added " + nw);
					}
				}
				catch (NullPointerException e) {
					//element has no name, ignore it
				}
			}
			currXmlNode = currXmlNode.getNextSibling();
		}
		
		// be recursive
		for (Tree<NodeWrapper> child : t.getSubTrees()) {
			populate(child);
		}
		//System.out.println("============");
	}

	/**
	 * returns a DOM node of the given platform. 
	 * @param p_name
	 * @return
	 */
	private Node getPlatformNode(String p_name) {
		
		NodeList p_nodes = this.document.getElementsByTagName("Platform");
		Node ret_node = null;
		for (int i=0; i < p_nodes.getLength(); i++) {
			Node current = p_nodes.item(i);
			try {
				String c_name = current.getAttributes().
						            getNamedItem("name").getNodeValue(); 
				if (c_name.equals(p_name)) {
					ret_node = current;
					break;
				}
			}
			catch (NullPointerException e){
				//probably no attribute named "name"
				System.err.println("Platfom file contains unnamed platforms!");
			}
		}
		return ret_node;
	}

	/**
	 * Writes the associated Platform File to disk, trimming beforehand
	 * @param output_f
	 */
	public void write(File output_f) {
		
		System.out.println("trimming");
		this.trim();
		
		System.out.print("writing...");
		DOMSource source = new DOMSource(this.document);
		StreamResult result = new StreamResult(output_f);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer     = tFactory.newTransformer();
			transformer.transform(source, result);
			
		} catch (TransformerConfigurationException e) {
			System.err.println("something went very wrong at the config");
			e.printStackTrace();
		} catch (TransformerException e) {
			System.err.println("something went very wrong at the transformation");
			e.printStackTrace();
		}
		
		System.out.println(" ok");
		
		this.restoreNodes();
		
	}
	
	/**
	 * Accessor to the DOM document
	 * @return
	 */
	public Document getDocument() {
		return document;
	}
	
	/**
	 * accessor to the final list
	 * @return
	 */
	public ArrayList<NodeWrapper> getFinalList() {
		return appliList;
	}
	
	/**
	 * Removes nodes from the document excluded nodes form the DOM Document.
	 * This method iterates on the list of applications and removes 
	 * all the DOM nodes from the document that are not marked as included.
	 * It does so by keeping a backup copy of the removed nodes.
	 * 
	 */
	private void trim() {
	
		for (NodeWrapper nw : appliList) {
			try {
				if (! nw.isIncluded()) {
					
					Node p = nw.getXmlNode().getParentNode();
					
					// remove the node from the Document and keep a backup copy in a separate list
					RestorableNode backup = new RestorableNode(p,
					                                           p.removeChild(nw.getXmlNode() )
					                                           ); 
					this.backupList.add(backup);
														
				}
			}
			catch (NullPointerException e) {
				//parent node has most likley been removed?
				//do nothing
			}
			
		}
	}
	
	/**
	 * restores any trimmed nodes
	 */
	private void restoreNodes() {
		System.out.println("restoring " + this.backupList.size() + " elements");
		
		for (RestorableNode rn : backupList) {
			try {
				rn.parent.appendChild(rn.removed);				
			}
			catch( Exception e) {
				System.out.println("Error: " + e);
			}
		}
		
		//after restoring nodes, empty the restore list
		this.backupList.clear();	
	}
	
}

/**
 * Helper class to be able to restore the nodes. 
 * @author saflores
 *
 */
class RestorableNode {
	Node parent;
	Node removed;
	
	RestorableNode(Node parent, Node removed) {
		
		if (null == parent || null == removed) {	
			throw new Error("Restorable node must have non-null attributes");
		}
		
		this.parent  = parent;
		this.removed = removed;
	}
	
	@Override
	public String toString() {
		return this.removed.getNodeName() + " son of " + this.parent.getNodeName() ;
	}
	
}
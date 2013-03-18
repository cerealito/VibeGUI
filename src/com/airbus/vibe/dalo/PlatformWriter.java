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
	private ArrayList<NodeWrapper>    finalList;
	private ArrayList<RestorableNode> backupList;
	
	public  Tree     <NodeWrapper> tree;

	/**
	 * Constructor with a file
	 * @param myFile
	 * @throws IOException if the passed string is not the path of a readable file
	 */
	public PlatformWriter(String path_s, String platform2trim) {
		
		DOMParser parser = new DOMParser();
		this.finalList = new ArrayList<NodeWrapper>();
		
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
						this.finalList.add(nw);
						
						System.out.println(">>> adding " + nw);
						// null pointer exception: why?
						//this.backupList.add(new RestorableNode(nw.getXmlNode().getParentNode(),
						//                                       nw.getXmlNode().cloneNode(true)));
						
						
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

	
	public void write(File output_f) {
		
		this.trim();
		
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
		
		this.restoreNodes();
		
	}
	
	public Document getDocument() {
		return document;
	}
	
	public ArrayList<NodeWrapper> getFinalList() {
		return finalList;
	}
	
	/**
	 * Removes nodes from the document that are not included
	 * in the nodeWrappers list
	 */
	private void trim() {
		
		for (NodeWrapper nw: finalList) {
			try {
				if (! nw.isIncluded()) {
					Node p = nw.getXmlNode().getParentNode();					
					p.removeChild(nw.getXmlNode());
				}
			}
			catch (NullPointerException e) {
				//parent node has most likley been removed?
				//do nothing
			}
			
		}
	}
	
	private void restoreNodes() {
		for (RestorableNode rn : backupList) {
			System.out.println(rn.toString());	
		}
	}
	
}

class RestorableNode {
	Node parent;
	Node copy;
	
	RestorableNode(Node parent, Node copy) {
		this.parent = parent;
		this.copy   = copy;
	}
	
	@Override
	public String toString() {
		return this.copy.getNodeValue();
	}
	
}
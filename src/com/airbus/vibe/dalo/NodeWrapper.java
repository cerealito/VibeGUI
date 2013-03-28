package com.airbus.vibe.dalo;

import org.w3c.dom.Node;

public class NodeWrapper{
	
	private Node    domNode;
	private boolean included;
	
	public NodeWrapper(Node domNode) {
		this.domNode = domNode;
		this.included = false;
	}
	
	public boolean isIncluded() {
		return included;
	}

	public void setIncluded(boolean included) {
		this.included = included;
	}

	public Node getXmlNode() {
		return domNode;
	}
	
    public String toString() {
		return this.actorName() + "  " + this.included;
    }

	public String actorName() {
		
		String actor_name="???";
		try {
			actor_name = domNode.getAttributes().
			                 getNamedItem("name").getNodeValue();
		}
		catch (NullPointerException e) {
			//node has no name, fuck it
		}
		return actor_name;
	}
	
	public String type() { 
		
		String type = "???";
		try {
			type = domNode.getAttributes().
			                 getNamedItem("type").getNodeValue();
		}
		catch (NullPointerException e) {
			//node has no type, fuck it
		}
		return type;
	}
	
	public boolean isEnvironment() {
		boolean ret = false;
		if ( domNode.getNodeName().equalsIgnoreCase("environment") ) {
			ret = true;
		}
		return ret;
	}
	
	public boolean isAppli() {
		boolean ret = false;
		if ( domNode.getNodeName().equalsIgnoreCase("appli") ) {
			ret = true;
		}
		return ret;
	}
	
	public boolean isComputer() {
		boolean ret = false;
		if ( domNode.getNodeName().equalsIgnoreCase("computer") ) {
			ret = true;
		}
		return ret;
	}
}

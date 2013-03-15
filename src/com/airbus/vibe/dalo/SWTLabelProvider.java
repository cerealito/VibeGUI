package com.airbus.vibe.dalo;

import mx.bitch.util.Tree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;
import org.w3c.dom.Node;

import com.airbus.vibe.gui.TsarGUI;

public class SWTLabelProvider implements ILabelProvider {


	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public String getText(Object element) {
		String r = "unreadable object " + element.getClass().toString();
		
		if (element instanceof Tree<?>) {
			NodeWrapper nw = ((Tree<NodeWrapper>)element).getElement(); 
			r = nw.actorName();
		}
		
		if ( element instanceof String) {
			r = (String)element;
		}
		
		return r;
	}

	public Image getImage(Object element) {
		Image img = null;
		
		if (element instanceof Tree<?>) {
			NodeWrapper nw = ((Tree<NodeWrapper>)element).getElement(); 
			
			if (nw.isComputer()) {
				
				if (nw.actorName().equalsIgnoreCase("linuxhost")) {
					img = SWTResourceManager.getImage(TsarGUI.class,
			                   "/com/airbus/vibe/gui/icons/network-server.png");
				}
				else {
					img = SWTResourceManager.getImage(TsarGUI.class,
					                 "/com/airbus/vibe/gui/icons/computer.png");
				}
			}
		}
		
		return img;
	}


}

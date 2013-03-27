package com.airbus.vibe.gui.listeners;

import mx.bitch.util.Tree;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import com.airbus.vibe.dalo.ActorsFileReader;
import com.airbus.vibe.dalo.NodeWrapper;
import com.airbus.vibe.gui.TsarGUI;


public class AdvSelectionDetailShower implements ISelectionChangedListener {
	TsarGUI gui;
	ActorsFileReader actorsReader;

	public AdvSelectionDetailShower (TsarGUI g) {
		this.gui = g;
		this.actorsReader = g.getAdvancedComposite().getActorsReader();
	}

	public void selectionChanged(SelectionChangedEvent e) {

		Object o = ((IStructuredSelection)e.getSelection()).getFirstElement(); 
		if ( o instanceof Tree<?> ) {
			NodeWrapper nw = ((Tree<NodeWrapper>) o).getElement(); 
			//TODO
			//gui.getAdvPropTableViewer().setInput();	
		}

		
		
	
		
	}

}
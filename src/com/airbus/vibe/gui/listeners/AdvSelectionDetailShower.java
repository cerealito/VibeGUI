package com.airbus.vibe.gui.listeners;

import mx.bitch.util.Tree;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import com.airbus.vibe.dalo.ActorWrapper;
import com.airbus.vibe.dalo.ActorsFileReader;
import com.airbus.vibe.dalo.NodeWrapper;
import com.airbus.vibe.gui.TsarGUI;
import com.sun.corba.se.spi.orbutil.fsm.InputImpl;


public class AdvSelectionDetailShower implements ISelectionChangedListener {
	TsarGUI gui;
	ActorsFileReader actorsReader;

	public AdvSelectionDetailShower (TsarGUI g) {
		this.gui = g;
	}

	public void selectionChanged(SelectionChangedEvent e) {

		Object o = ((IStructuredSelection)e.getSelection()).getFirstElement(); 
		if ( o instanceof Tree<?> ) {
			NodeWrapper nw = ((Tree<NodeWrapper>) o).getElement(); 
			
			//actorsReader may change so we get it at every selection event
			this.actorsReader = gui.getAdvancedComposite().getActorsReader();

			if (null != this.actorsReader ) {
				ActorWrapper[] inputItems = this.actorsReader.getActors(nw.actorName());
				gui.getAdvPropTableViewer().setInput(inputItems);
			}
			else {
				System.out.println("actorsReader not initialized");
			}				
		}

		
		
	
		
	}

}
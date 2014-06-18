package com.airbus.vibe.gui.listeners;

import mx.bitch.util.Tree;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.dalo.ActorsFileReader;
import com.airbus.vibe.dalo.NodeWrapper;
import com.airbus.vibe.dalo.PlatformWriter;
import com.airbus.vibe.gui.TsarGUI;

public class FileRefresher implements Listener {
	
	TsarGUI gui;
	CheckboxTreeViewer viewer;

	
	public FileRefresher (TsarGUI g) {
		this.gui = g;
		this.viewer = this.gui.getAdvTreeViewer();
	}
	
	public void handleEvent(Event e) {
		NodeWrapper previous_nw = null;
		Object o = ((IStructuredSelection)this.viewer.
				getSelection()).getFirstElement(); 
		if ( o instanceof Tree<?> ) {
			previous_nw = ((Tree<NodeWrapper>) o).getElement();
		}

		// ##### BEGIN REFRESH: copied from AdvModeActivator
		//                  will reread and refresh the tree 
		//get the selected platform
		String p_file = gui.getAdvLblCurrentPlatform().getText();
		String p_name = gui.getComboPlatform().getText();
		
		//this was set before hand
		String a_name = gui.getAdvancedComposite().getAdvStrCurrentActorsFile();
		
		//create the PlatformWriter
		PlatformWriter pw = new PlatformWriter(p_file, p_name);
		
		//create the actors file reader
		ActorsFileReader afr = new ActorsFileReader(a_name);
		gui.getAdvancedComposite().setActorsReader(afr);
		
		System.out.println(pw.tree);
		
		if ( ! pw.tree.getSubTrees().isEmpty() ) {
			viewer.setInput(pw.tree);
			gui.getController().setAdvLaunchPW(pw);
		}
		else {
			viewer.setInput("Platform " + p_name 
					         + " has no elements defined");
			gui.getController().setAdvLaunchPW(null);
			gui.getBtnLaunch().setEnabled(false);
		}
		
		viewer.expandAll();
		// start with every item UNCHECKED
		// ##### END REFRESH ###

		viewer.setComparer(new IElementComparer() {
			
			public int hashCode(Object arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			public boolean equals(Object a, Object b) {

				if ( a instanceof Tree<?> && b instanceof Tree<?>) {
					NodeWrapper nw_a = ((Tree<NodeWrapper>) a).getElement();
					NodeWrapper nw_b = ((Tree<NodeWrapper>) b).getElement();
					if (nw_a.actorName().equals(nw_b.actorName()) &&
						nw_a.type().equals(nw_b.type())
							) {
						return true;
					}
				}
				return false;
					
			}
		});
			
		if (null != previous_nw) {
			System.out.println(" ####> restoring " + previous_nw + " from " + o);
			StructuredSelection ss = new StructuredSelection(o);
			viewer.setSelection(ss, true);
		}
		
		if (viewer.getSelection().isEmpty()) {
			gui.getAdvPropTableViewer().setInput(null);
		}

	}

}

package com.airbus.vibe.gui.listeners;

import mx.bitch.util.Tree;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;



import com.airbus.vibe.dalo.NodeWrapper;
import com.airbus.vibe.gui.TsarGUI;

public class AdvTreeViewerChecker implements ICheckStateListener {
	private TsarGUI gui;
	
	public AdvTreeViewerChecker(TsarGUI g) {
		this.gui = g;
	}
	
	public void checkStateChanged(CheckStateChangedEvent event) {

		if ( event.getElement() instanceof Tree<?> ) {
			//report back to platform writer
			
			Tree<NodeWrapper> tree = ((Tree<NodeWrapper>)event.getElement()); 		
			tree.getElement().setIncluded(event.getChecked());
			
			System.out.println(tree.getElement());
			//do the same for children, not recursive for now
			for (Tree<NodeWrapper> child_t : tree.getSubTrees()) {
				child_t.getElement().setIncluded(event.getChecked());
				System.out.println("\t" + child_t);
			}
			System.out.println("\n");
			// do it visually as well
			if ( event.getCheckable() instanceof CheckboxTreeViewer) {
				CheckboxTreeViewer t=((CheckboxTreeViewer)event.getCheckable());
				t.setSubtreeChecked(event.getElement(), event.getChecked());					
			}
			
		}

	}

	

}

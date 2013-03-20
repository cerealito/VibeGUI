package com.airbus.vibe.gui.listeners;

import mx.bitch.util.Tree;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.dalo.NodeWrapper;
import com.airbus.vibe.gui.TsarGUI;

public class btnLaunchAction implements Listener {

	TsarGUI gui;
	
	
	public btnLaunchAction(TsarGUI g) {
		this.gui = g;
	}
	
	
	public void handleEvent(Event e) {
		
		// upon launch, select the ouptut tab
		gui.getSimuFolder().setSelection(gui.getTabOutput());
		
		if (gui.getCheckBoxAdvMode().getSelection()){
			//advanced mode
			
			// call the adv launch method on the controller
			gui.getController().launchAdvanced(gui.getComboApp().getText(),
                                               gui.getComboPlatform().getText()
                                               );
			// uncheck everyone from the tree
			Object[] objs = gui.getAdvTreeViewer().getCheckedElements();		
	
			
			for (Object o : objs) {
				if (o instanceof Tree) {
					//uncheck visually:
					gui.getAdvTreeViewer().setChecked(o, false);
					// ??? how to notify the listener?
					// uncheck in the model:
					((NodeWrapper) ((Tree<NodeWrapper>)o).getElement()).setIncluded(false) ;
					System.out.println("    " + o);
				}
			}
		}
		else {
			//normal mode
			gui.getController().launchPlatform(gui.getComboApp().getText(),
					                           gui.getComboPlatform().getText());
		}
	}
}

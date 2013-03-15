package com.airbus.vibe.gui.listeners;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

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
		}
		else {
			//normal mode
			gui.getController().launchPlatform(gui.getComboApp().getText(),
					                           gui.getComboPlatform().getText());
		}
	}
}

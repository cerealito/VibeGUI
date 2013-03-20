package com.airbus.vibe.gui.listeners;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.TsarGUI;



public class btnKillAction implements Listener {

	TsarGUI gui;
	
	/**
	 * @param gui
	 */
	public btnKillAction(TsarGUI gui) {
		this.gui = gui;
	}

	public void handleEvent(Event event) {
		gui.getController().kill_All(gui.getComboApp().getText(),
				                     gui.getComboPlatform().getText()
				                     );
	}

}
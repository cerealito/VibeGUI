package com.airbus.vibe.gui.listeners;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.TsarGUI;

public class btnFireAction implements Listener {

	TsarGUI gui;
	
	public btnFireAction(TsarGUI g) {
		this.gui = g;
	}
	public void handleEvent(Event event) {

		gui.getScenarioFolder().setSelection(gui.getTabExec());
		gui.getController().launchScenario(gui.getComboScenario().getText());
	}

}

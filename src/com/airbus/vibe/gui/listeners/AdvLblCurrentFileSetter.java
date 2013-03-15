package com.airbus.vibe.gui.listeners;

import java.io.File;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.TsarGUI;
import com.airbus.vibe.gui.Constants;

public class AdvLblCurrentFileSetter implements Listener {
	TsarGUI gui;
	Combo comboApp;
	File  all_app_dir;

	public AdvLblCurrentFileSetter (TsarGUI g) {
		this.gui = g;
		this.comboApp      = g.getComboApp();
		this.all_app_dir   = new File(Constants.app_dir);
	}
	
	public void handleEvent(Event event) {
		
		String selected_app = comboApp.getText();
		
		File p_f = new File(all_app_dir.getAbsolutePath() + "/" +
                      selected_app + "/" +
                      "Platforms_" + selected_app + ".xml");

		if (p_f.canRead()) {
			gui.getAdvLblCurrentPlatform().setText(p_f.getAbsolutePath());
		}		

	}

}

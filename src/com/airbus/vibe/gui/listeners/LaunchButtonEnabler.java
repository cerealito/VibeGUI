package com.airbus.vibe.gui.listeners;

import java.io.File;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.Constants;
import com.airbus.vibe.gui.TsarGUI;


public class LaunchButtonEnabler implements Listener {

	Button btnLaunch;
	Combo  comboApp;
	Combo  comboPlatform;
	File   all_app_dir;
	boolean online;
	
	public LaunchButtonEnabler(TsarGUI g) {
		btnLaunch     = g.getBtnLaunch();
		comboApp      = g.getComboApp();
		comboPlatform = g.getComboPlatform();
		all_app_dir   = new File(Constants.app_dir);
		online = g.isOnline();
	}
	
	public void handleEvent(Event e) {	
		// get the absolute path of the selected platform
		//  the platform items are prefilled. 
		String selected_app = this.comboApp.getText();
		
		try {
			File p_f = new File(all_app_dir.getAbsolutePath() + "/" +
			           selected_app + "/" +
			           "Platforms_" + selected_app + ".xml");

			if (p_f.canRead() && !online) {
				btnLaunch.setEnabled(true);
			}
		}
		catch (IllegalArgumentException iae) {
			//nothing is selected but the event took place:
			// this means we're being called at the very begining
			// do nothing.
		} 
		
	}
	
}

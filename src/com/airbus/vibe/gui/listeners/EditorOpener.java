package com.airbus.vibe.gui.listeners;

import java.io.File;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.TsarGUI;

/**
 * Opens a Text editor on the given file
 * 
 *
 */
public class EditorOpener implements Listener {

	Combo comboScenario;

	
	public EditorOpener(TsarGUI g) {
		comboScenario = g.getComboScenario();

	}
	
	public void handleEvent(Event event) {
		File f = new File(comboScenario.getText());
		if (f.exists()) {
			
			try{
				Runtime.getRuntime().exec("nedit " + f.getAbsolutePath());
			}
			catch (Exception e) {
				System.err.println("Error setting permissions");
				System.err.println("Error: " + e.getMessage());
			}		
		}
		else {
			//do nothing
		}
	}
}

package com.airbus.vibe.gui.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.TsarGUI;

public class btnBrowseAction implements Listener {


	TsarGUI gui;
	private FileDialog fileChooser;

	public btnBrowseAction(TsarGUI g) {
		this.gui = g;
	}
	
	public void handleEvent(Event event) {

		String f = null;

		fileChooser = new FileDialog(gui.getShell(), SWT.OPEN);
		fileChooser.setFilterPath("/home/SIMU_VIBE/" +
				"EXTERNAL_APPLICATIONS/TSAR/" +
				"Scenarios");
		String[] extensions = {"*.tsr"};
		fileChooser.setFilterExtensions(extensions);
		fileChooser.setText("Choose a TSAR Script");


		if ((f = fileChooser.open()) != null) {
			//user did not cancelled
			gui.getComboScenario().setText(f);
			gui.getComboScenario().add(f, 0);
		}


	}

}

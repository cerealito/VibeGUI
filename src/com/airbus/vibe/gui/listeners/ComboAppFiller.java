package com.airbus.vibe.gui.listeners;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.PatternSyntaxException;


import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.Config;
import com.airbus.vibe.gui.TsarGUI;
import com.airbus.vibe.gui.Constants;

public class ComboAppFiller implements Listener {

	TsarGUI    gui;
	Combo comboApp;
	File  all_app_dir;
	
	public ComboAppFiller(TsarGUI g) {
		this.gui = g;
		this.comboApp = this.gui.getComboApp();
		this.all_app_dir  = new File(Constants.app_dir);
		
		this.handleEvent(null);
	}
	
	public void handleEvent (Event e) {
		// start over every time
		comboApp.removeAll();
		
		String[] children = all_app_dir.list();

		if (children == null) {
			// Either dir does not exist or is not a directory
			// TODO: emit a warning
			System.err.println("No apps!");
		} else {			
			
			Arrays.sort(children);

			for (int i = 0; i < children.length; i++) {

				// Get filename of file or directory
				String myApp = all_app_dir.getAbsolutePath() + "/" + children[i];
				File myAppDir = new File(myApp);
				
				// if it is not a directory; skip
				if ( ! myAppDir.isDirectory())
					continue;

				// use a file filter to check for xml files
				FilenameFilter ff = new FilenameFilter() {
					
					public boolean accept(File dir, String name) {
						String af = "Applications_" + dir.getName() + ".xml";
						String pf = "Platforms_"    + dir.getName() + ".xml";
						
						
						if (name.equals(af) ||
							name.equals(pf)   )
							return true;
						else
							return false;
					}
				};
				
				// if xml files not named as we want; skip
				if ( myAppDir.listFiles(ff).length != 2)
					continue;

				// ####################################################
				// # 1. hosts.allow: search which apps to show:
				// ####################################################					

				//check in the config if there is a filter
				Config c = Config.getConfig();
				String pass_regexp  = c.get("app_pass");
				String block_regexp = c.get("app_block");
									
				if ( pass_regexp == null ) {
					// if there is no pass filter; just add the app
					comboApp.add(myAppDir.getName());
				}
				
				else {
					//there is a pass filter, see if we match
					try {					
						if (myAppDir.getName().matches(pass_regexp) ) {								
							comboApp.add(myAppDir.getName());
						}
						//otherwise just skip it
					}
					catch(PatternSyntaxException exception) {
						System.err.println("Warning: Config file " +
								           "contains invalid regexp for " +
								           "key app_filter. Filter ignored");
						comboApp.add(myAppDir.getName());
					}
				}
					
				// ####################################################
				// # 2. hosts.deny: search which apps NOT to show
				// ####################################################
				if ( block_regexp == null ) {
					// if there is no filter; do noting
				}
				
				else {
					//there is a block filter, see if we match
					try {
						if (myAppDir.getName().matches(block_regexp) ) {
							comboApp.remove(myAppDir.getName());						
						}
						//otherwise just skip it
					}
					catch(PatternSyntaxException exception) {
						System.err.println("Warning: Config file " +
								           "contains invalid regexp for " +
								           "key app_filter. Filter ignored");
					}
				}
			}			
		}
	}
}

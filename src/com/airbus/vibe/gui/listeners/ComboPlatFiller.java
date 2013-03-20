package com.airbus.vibe.gui.listeners;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.dalo.PlatformFastReader;
import com.airbus.vibe.gui.Constants;
import com.airbus.vibe.gui.TsarGUI;

public class ComboPlatFiller implements Listener {

	TsarGUI gui;
	Combo comboPlatform;
	Combo comboApp;
	File  all_app_dir;

	public ComboPlatFiller (TsarGUI g) {
		this.gui = g;
		this.comboPlatform = g.getComboPlatform();
		this.comboApp      = g.getComboApp();
		this.all_app_dir   = new File(Constants.app_dir);
	}
	
	/**
	 * This method is fired when Combo app is selected
	 * --> it populates the combo platform
	 */
	public void handleEvent(Event event) {
		
		String selected_app = this.comboApp.getText();
		
		this.comboPlatform.removeAll();
		
		File p_f = new File(all_app_dir.getAbsolutePath() + "/" +
		                    selected_app + "/" +
		                    "Platforms_" + selected_app + ".xml");
			
		if (p_f.canRead()) {
			PlatformFastReader reader;
			reader = new PlatformFastReader(p_f.getAbsolutePath());

			for (String p : reader.getPlatforms())
				this.comboPlatform.add(p);
		}
		else {
			System.err.println(p_f.getAbsolutePath() + " not there ");
		}
		
		if( ! this.fixPlatformIfNeeded()) {
			if (comboPlatform.getItemCount() > 0) {		
				comboPlatform.select(comboPlatform.getItemCount()-1);
				comboPlatform.notifyListeners(SWT.Selection, new Event());
			}
		}
			
		
	}

	/**
	 *	if we are in vibe{1,2,3} select that and disable anything else 
	 *  @return true if the platform was fixed 
	 * 
	 */
	private boolean fixPlatformIfNeeded() {
			
		String hostname = System.getenv().get("HOSTNAME"); 
		boolean found = false;
		boolean changeAllowed = false;
		
		if (hostname != null) {

			int idxPlat = -1;
			if (hostname.equalsIgnoreCase("vibe1")) {
				for (String i : comboPlatform.getItems()) {
					idxPlat++;
					if(i.equalsIgnoreCase("vibe_debug1")) {
						System.out.println("limiting platform to vibe_debug1");
						break;
					}
				}
			}
			
			else if (hostname.equalsIgnoreCase("vibe10")) {
				for (String i : comboPlatform.getItems()) {
					idxPlat++;
					if(i.equalsIgnoreCase("vibe1")){
						System.out.println("limiting platform to vibe1");
						break;
					}
				}
			}
			
			else if (hostname.equalsIgnoreCase("vibe20")) {
				for (String i : comboPlatform.getItems()) {
					idxPlat++;
					if(i.equalsIgnoreCase("vibe2")) {
						System.out.println("limiting platform to vibe2");
						break;
					}
				}
			}
			
			else if (hostname.equalsIgnoreCase("aieaer44")) {
				for (String i : comboPlatform.getItems()) {
					idxPlat++;
					if(i.equalsIgnoreCase("vibe3")) {
						System.out.println("limiting platform to vibe3");
						break;
					}
				}
			}

			else if (hostname.equalsIgnoreCase("coleoptere")) {
				for (String i : comboPlatform.getItems()) {
					idxPlat++;
					if(i.equalsIgnoreCase("vador")) {
						System.out.println("suggesting platform vador");
						changeAllowed = true;
						break;
					}
				}
			}

			// idxPlat will be non-negative ONLY IF found the appropriate platform
			if (idxPlat >= 0) {
				comboPlatform.select(idxPlat);
				comboPlatform.notifyListeners(SWT.Selection, new Event());

				if ( ! changeAllowed ) {
					comboPlatform.setEnabled(false);
				}
				
				found = true;
			}
		}
		
		return found;
	}
}

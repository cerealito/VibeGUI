package com.airbus.vibe.gui.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import com.airbus.vibe.gui.TsarGUI;

/**
 * The listener that activates the fire button and fills the editor view
 * upon action in the scenario combo box
 * @author saflores
 *
 */
public class ComboScenarioAction implements ModifyListener {

	Combo      comboScenario;
	StyledText editorText;
	Button     btnFire;
	TsarGUI    gui;
	
	public ComboScenarioAction(TsarGUI g) {
		this.comboScenario = g.getComboScenario();
		this.editorText    = g.getEditorText();
		this.btnFire       = g.getBtnFire();
		this.gui           = g;
	}
	
	public void modifyText(ModifyEvent e) {
		File f = new File(comboScenario.getText());
		
		editorText.setText("");
		
		if (f.exists() ) {
			if (f.getName().endsWith(".tsr") && gui.isOnline()) {
				btnFire.setEnabled(true);
				
				try {
					FileReader r = new FileReader(f);
					BufferedReader br = new BufferedReader(r);
					
					
					String l = br.readLine(); 

					while( l != null ) {            	
						editorText.append(l+'\n');
						l = br.readLine();
					}
					
					
				} catch (FileNotFoundException e1) {
					System.err.println("Tsar script was deleted");
				}
				catch (IOException e2) {
					System.err.println("Tsar script unaccessible," +
							"check your permissions");
				}				
			}
		}
		else {
			System.err.println("file does not exist");
			btnFire.setEnabled(false);
		}
	}
}

package com.airbus.vibe.gui.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.gui.TsarGUI;

/**
 * Opens a Text editor on the given file
 * 
 *
 */
public class EditorRefresher implements Listener {

	StyledText editorText;
	Combo   comboScenario;
	
	public EditorRefresher(TsarGUI g) {
		editorText = g.getEditorText();
		comboScenario = g.getComboScenario();
	}
	
	public void handleEvent(Event event) {

		File f = new File(comboScenario.getText());
		
		editorText.setText("");
		
		if (f.exists() ) {
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
}


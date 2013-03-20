package com.airbus.vibe.gui.listeners;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

import com.airbus.vibe.gui.TsarGUI;

public class OnlineDetector implements PaintListener {
	Button btnLaunch;
	Label  lblStatus;
	TsarGUI g;
	
	public OnlineDetector(TsarGUI g) {
		btnLaunch = g.getBtnLaunch();
		lblStatus = g.getLblStatus();

		this.g = g;
	}
	
	public void paintControl(PaintEvent e) {
		if(lblStatus.getText().endsWith("online")) {
			// #### when going online:
			
			// set the gui state to online so that the other listeners can know
			// for now, at least EnableLaunchButton listener must read this
			// state.
			g.setOnline(true);
			
			// Disable messing with App and Plat combos: we need them for the kill
			g.getComboApp().setEnabled(false);
			g.getComboPlatform().setEnabled(false);
			
			// disable the adv button
			g.getBtnAdvancedMode().setEnabled(false);
			
			// advanced/basic mode differences:
			if (g.getCheckBoxAdvMode().getSelection()){
				// #### Advanced mode:
				// DO NOTHING
			}
			else {
				// #### Normal mode
				
				// Disable re-launching, because we're already online!
				btnLaunch.setEnabled(false);
				
				// disable the tree so that no stupid things can be done ;)
				g.getAdvTreeViewer().getTree().setEnabled(false);
			}
			
		}
		else {
			// #### when going offline:
			//notify the rest of the simulation we're offline
			g.setOnline(false);
						
			// Re-enable combos and Launch button:
			g.getComboApp().setEnabled(true);
			g.getComboPlatform().setEnabled(true);
			
			// Re-enable the adv button
			g.getBtnAdvancedMode().setEnabled(true);
			
			new LaunchButtonEnabler(g).handleEvent(null);
		}
	}

}


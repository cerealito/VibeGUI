package com.airbus.vibe.gui.listeners;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import org.eclipse.swt.widgets.Listener;

import com.airbus.vibe.dalo.PlatformWriter;
import com.airbus.vibe.gui.TsarGUI;

/**
 * This establishes the behaviour of the gui upon selection/deselection
 * of the Advanced Mode checkbox
 * @author saflores
 *
 */
public class AdvModeActivator implements Listener {

	private TsarGUI gui;
	CheckboxTreeViewer viewer;

	
	public AdvModeActivator(TsarGUI g) {
		this.gui = g;
		this.viewer = this.gui.getAdvTreeViewer();
	}
	
	public void handleEvent(Event event) {
		
		if(gui.getBtnAdvancedMode().getSelection()) {
			// #### #### Adv Mode Checkbox has been checked
			// #####################################################
			// activate the contents of the adv tab on selection:			
			gui.getAdvLblCurrentPlatform().setEnabled(true);
			gui.getAdvLblInfo1().setEnabled(true);
			gui.getAdvTreeViewer().getTree().setEnabled(true);
			gui.getSimuFolder().setSelection(gui.getTabAdvanced());
			gui.getTabAdvanced().setText("Advanced");
			
			//get the selected app and plat
			String p_file = gui.getAdvLblCurrentPlatform().getText();
			String p_name = gui.getComboPlatform().getText();
			
			PlatformWriter pw = new PlatformWriter(p_file, p_name);
			
			System.out.println(pw.tree);
			
			if ( ! pw.tree.getSubTrees().isEmpty() ) {
				viewer.setInput(pw.tree);
				gui.getController().setAdvLaunchPW(pw);
			}
			else {
				viewer.setInput("Platform " + p_name 
						         + " has no elements defined");
				gui.getController().setAdvLaunchPW(null);
				gui.getBtnLaunch().setEnabled(false);
			}
			
			viewer.expandAll();
			viewer.setAllChecked(true);
		}
		else {
			// #### #### Adv Mode Checkbox has been unchecked
			// #####################################################
			gui.getAdvTreeViewer().setInput(null); // this effectively clears the contents of advTree
			//gui.getAdvTreeViewer().getTree().clearAll(true); // not needed because input is null
			gui.getAdvLblCurrentPlatform().setEnabled(false);
			gui.getAdvLblInfo1().setEnabled(false);
			gui.getAdvLblSelectComponents().setEnabled(false);
			//gui.getAdvTreeViewer().getTree().setEnabled(false); // not needed because input is null
			gui.getSimuFolder().setSelection(gui.getTabOutput());
			gui.getTabAdvanced().setText("");
		}
		
	}

}

package com.airbus.vibe.gui;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			
			configure();
			
			Controller control = new Controller();
			TsarGUI    myGUI = new TsarGUI(control);

			control.setGUI(myGUI);
			
			Pinger p = new Pinger(myGUI);
			
			// ping once ------------------
			try {
				System.out.print("Checking for VIBE simulations on this host... ");
				if (p.ping()) {
					System.out.println("ERROR!");
					System.err.println("There are simulations running on this host");
					System.err.println("Kill all simulations before continuing");
					
					//add dialog
					Status status = new Status(IStatus.ERROR,
			                   "TSAR GUI",
			                   0,
			                   "Only one simulation per host can be running " +
			                   "at any given time",
			                   null);
	
					ErrorDialog.openError(myGUI.getShell(),
							"Error",
							"Kill all simulations before continuing",
							status);

					
//					System.exit(-1);
				}
				else {
					System.out.println("ok");
				}
				
			} catch (InterruptedException e) {
				System.out.println("\n");
				System.err.println("bye");
				Tools.removeTmpFile(p.getScript());
				System.exit(0);
				
			} catch (IOException e) {
				System.out.println("\n");
				// perhaps not a vibe host
				// add dialog
				
				Status status = new Status(IStatus.ERROR,
						                   "TSAR GUI",
						                   0,
						                   "This program should be run on " +
						                   "a Linux host",
						                   null);
				
				ErrorDialog.openError(myGUI.getShell(),
						              "Error",
						              "Not in Linux",
						              status);
				
				System.err.println("Can not execute. " +
						"Maybe you're not in a vibe host?");
				Tools.removeTmpFile(p.getScript());
				System.exit(-1);
			}
			// if we get here, everything seems ok			
			
			
			//start to survey the state of the simulation
			p.start();
			
			myGUI.open();
			
			p.interrupt();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void configure() {

		System.out.println("reading config... ");
		
		Config myConfig = Config.getConfig();
		
		File tmpDir_f   = new File(myConfig.get("tmp_dir"));
		
		if (tmpDir_f.exists()) {
			//exists but is not a dir
			if( ! tmpDir_f.isDirectory()) {
		
				System.out.println("ERROR!");
				System.exit(-1);
			}
			//exists but can not r/w
			if ( !tmpDir_f.canWrite() ||
				 !tmpDir_f.canRead()) {
				System.out.println("ERROR!");
				System.exit(-1);				
			}
		}
		else {
			tmpDir_f.mkdirs();
		}
	}

}

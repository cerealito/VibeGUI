package com.airbus.vibe.gui;

import java.io.File;

public class Tools {

	/**
	 * Adds executePermission to a file
	 * @param f_name
	 */
	public static void chmod(String mode, String f_name) {
		try{
			Process p  = Runtime.getRuntime().exec("chmod " + mode + " "+f_name);
			p.waitFor();
		}
		catch (Exception e) {
			System.err.println("Error setting permissions");
			System.err.println("Error: " + e.getMessage());
		}		
	}
	
	/** ***********************************************************************
	 * tags temporary launcher files for deletion on exit
	 * @param f the file that will be removed 
	 */
	public static void removeTmpFile(String f) {
		File launcher = new File(f);		
		if (launcher.exists()) {
			launcher.deleteOnExit();
		}		
	}

}

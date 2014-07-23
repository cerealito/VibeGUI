package com.airbus.vibe.gui;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

public final class Config {
	
	private static Config instance = null;
	private static LinkedHashMap<String, String> dict = 
			new LinkedHashMap<String, String>();
	
	/**
	 * classic singleton
	 * 
	 */
	private Config() {
		// ########### internal variables
	
		String home_str = System.getenv().get("HOME");
	
		if (home_str == null) {
			//sensible default
			home_str = Constants.fallback_home;
		}

		// 1. the tmp dir
		dict.put("tmp_dir", home_str + "/.tsarGUI");
		
		String conf_file_str = dict.get("tmp_dir") + "/gui.conf";				
		// 2. the user config file
		dict.put("conf_f", conf_file_str );
		
	}
	
	/**
	 * classic singleton: call this to get the instance
	 * @return the only config object
	 */
	public static Config getConfig() {
		if (instance == null) {
			instance = new Config();
		}
		
		// ############ read user conf
		readUserConf();
		
		return instance;
	}
	
	public String get(String k) {
		return dict.get(k);
	}
	
	private static void readUserConf() {
		
		Properties p = new Properties();
		
		try {
			
			p.load(new FileInputStream(dict.get("conf_f")));
			
			for ( Object o : p.keySet() ) {
				dict.put((String)o, (String)p.get(o));
			}
			

		} catch (FileNotFoundException e) {
			// user conf does not exist or cant be read.
			// do nothing.
		} catch (IOException e) {
			// user conf does not exist or cant be read.
			// do nothing.
		}

	}
	
	public static void printConf() {
		for (String s : dict.keySet()) {
			System.out.println(s + ":\t" + dict.get(s));
		}
	}
	
}

package com.airbus.vibe.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import com.airbus.vibe.dalo.PlatformWriter;

public class Controller {
	
	// Put this constants in the Constants class? : no these concern the controller only
	private static final String KILL_ALL    = "kill_all";
	private static final String LAUNCH_SIMU = "launch_simu";
	private static final String LAUNCH_TSAR = "launch_tsar";
	private static final String ADV_PLATXML = "platform_xml";
		
	TsarGUI gui = null;
	String  app = "";
	String  platform = "";
	Config  cnf = Config.getConfig();
	private PlatformWriter advPlatformWriter;

	
	static int count = 1;
	/** ************************************************************************
	 * Sets the GUI
	 * @param pw
	 */
	public void setGUI(TsarGUI gui) {
		this.gui = gui;
	}
	

	/** ************************************************************************
	 * Sets the platform writter to be used by advLaunch
	 * @param pw
	 */
	public void setAdvLaunchPW(PlatformWriter pw) {
		this.advPlatformWriter = pw;
	}
	
	/** ************************************************************************
	 * Calls the launch_Tsarin.ksh script and starts separate threads to
	 * handle output and error 
	 * 
	 * @param s_param
	 */
	public void launchScenario(String s_param) {
		
		File scenario = new File(s_param);
		String f;
		
		f = generate_scenario_launcher(this.app, this.platform, s_param);

		String cmd = cnf.get("tmp_dir") + "/" + LAUNCH_TSAR + this.hashCode() + ".bat";
		ProcessWrapper out = new ProcessWrapper(cmd,
				this.gui.getTsarText(),
				this.gui);
		
		// fire the new thread and return asap
		// remember: it is crucial that any time-consuming operations are made
		// in the external thread and not in this one! otherwise bloody GUI will
		// freeze
		out.start();
		
		Tools.removeTmpFile(f);
		
	}
	
	/** ************************************************************************
	 * Launches the simu in adv mode
	 * @param app
	 * @param platform
	 */
	public void launchAdvanced(String app, String platform) {
		this.app = app;
		this.platform = platform;
		
		System.out.println("Advanced launch!!!");
		// trim the file according to adv viewer selection
		if (this.advPlatformWriter == null) {
			// empty platform or method called Before
			// this.setAdvLauncPW
			System.out.println("empty platform?");
		}
		else {
			System.out.println("\n=========================");
			System.out.println(this.advPlatformWriter.tree);
			//System.out.println("===");
			//System.out.println("===");
			//System.out.println(this.advPlatformWriter.o_tree);
			System.out.println("=========================\n");

			// output to a temp file in the hidden dir
			String p_xml = this.generate_adv_plat_file();

			// call dalo with the same Apps but the temp Plat 
			String f;
			
			f = generate_adv_launcher(this.app, this.platform, p_xml);
			
			String cmd = cnf.get("tmp_dir") + "/" + LAUNCH_SIMU + this.hashCode() + ".bat";
			ProcessWrapper out = new ProcessWrapper(cmd,
					                                this.gui.getSimuText(),
					                                this.gui);
			out.start();

			Tools.removeTmpFile(f);
			Tools.removeTmpFile(p_xml);
		}
	}

	
	
	/** ************************************************************************
	 * 
	 * @param app
	 * @param platform
	 */
	public void launchPlatform(String app, String platform) {
		
		this.app = app;
		this.platform = platform;
		String f;
		
		f = generate_simu_launcher(this.app, this.platform);
		
		String cmd = cnf.get("tmp_dir") + "/" + LAUNCH_SIMU + this.hashCode() + ".bat";
		ProcessWrapper out = new ProcessWrapper(cmd,
				this.gui.getSimuText(),
				this.gui);
		out.start();		 

		Tools.removeTmpFile(f);
		
	}

	/** ************************************************************************
	 * 
	 * @param app
	 * @param platform
	 */
	public void kill_All(String app, String platform) {
		
		this.app = app;
		this.platform = platform;
		String f;
		
		f = generate_kill_all(this.app, this.platform);

		String cmd = cnf.get("tmp_dir") + "/" + KILL_ALL + this.hashCode() + ".bat";
		ProcessWrapper out = new ProcessWrapper(cmd,
				this.gui.getSimuText(),
				this.gui);
		out.start();		 

		Tools.removeTmpFile(f);
		
	}
		
	/** ************************************************************************
	 * generates a temporary shell script that launches the simu
	 * @param app
	 * @param platform
	 */
	private String generate_adv_launcher(String app, String platform, String platform_file_p) {
		
		String f_name = cnf.get("tmp_dir") + "/" + LAUNCH_SIMU + this.hashCode() + ".bat";
		
		File launch_f = new File(f_name);

		if (launch_f.exists()){
			launch_f.delete();
		}
		
		try {
			launch_f.createNewFile();

			BufferedWriter w = new BufferedWriter(new FileWriter(launch_f));
			w.write("@echo off");
			w.newLine();
			
			String af = Constants.app_dir + "/" + app + "/Applications_" + app + ".xml";
			
			
			w.write("python " + Constants.dalo_exe  +
					" -p " + platform_file_p +
					" -a " + af              +
					" -P " + platform        +
					" -m launch");
			
			w.newLine();
			
			w.newLine();
			
			w.flush();			
			w.close();

		}
		catch (Exception e) {
			System.err.println("Error writing temporary file - check your permissions");
			System.err.println("Error: " + e.getMessage());
		}
		
		Tools.chmod("+x", f_name);
		
		if (launch_f.canRead()) {
			return f_name;
		}
		else {
			System.err.println("Error generating an executable file!");
			return "";
		}
	}
	
	
	
	/** ************************************************************************
	 * 
	 * 
	 * 
	 */
	private String generate_adv_plat_file() {
		String f_name = cnf.get("tmp_dir") + "/" + ADV_PLATXML + this.hashCode();
		
		File temp_xml = new File(f_name);

		if (temp_xml.exists()){
			temp_xml.delete();
		}
		
		try {
			temp_xml.createNewFile();
		}
		catch (Exception e) {
			System.err.println("Error writing temporary file - check your permissions");
			System.err.println("Error: " + e.getMessage());
		}
		
		this.advPlatformWriter.write(temp_xml);
		
		if (temp_xml.canRead()) {
			return f_name;
		}
		else {
			System.err.println("Error generating an executable file!");
			return "";
		}
	}


	/** ************************************************************************
	 * generates a temporary shell script that launches the simu
	 * @param app
	 * @param platform
	 */
	private String generate_simu_launcher(String app, String platform) {
		
		String f_name = cnf.get("tmp_dir") + "/" + LAUNCH_SIMU + this.hashCode() + ".bat";
		
		File launch_f = new File(f_name);

		if (launch_f.exists()){
			launch_f.delete();
		}
		
		try {
			launch_f.createNewFile();

			BufferedWriter w = new BufferedWriter(new FileWriter(launch_f));
			w.write("@echo off");
			w.newLine();
			
			String af = Constants.app_dir + "/" + app + "/Applications_" + app + ".xml";
			String pf = Constants.app_dir + "/" + app + "/Platforms_" + app + ".xml";
			
			w.write("python " + Constants.dalo_exe +
					" -p " + pf             +
					" -a " + af             +
					" -P " + platform       +
					" -m launch");
			
			w.newLine();
			
			w.newLine();
			
			w.flush();			
			w.close();

		}
		catch (Exception e) {
			System.err.println("Error writing temporary file - check your permissions");
			System.err.println("Error: " + e.getMessage());
		}
		
		Tools.chmod("+x", f_name);
		
		if (launch_f.canRead()) {
			return f_name;
		}
		else {
			System.err.println("Error generating an executable file!");
			return "";
		}
	}
	
	/** ************************************************************************
	 * 
	 * @param app
	 * @param platform
	 * @return
	 */
	private String generate_kill_all(String app, String platform) {

		String f_name = cnf.get("tmp_dir") + "/" + KILL_ALL + this.hashCode() + ".bat";
		
		File launch_f = new File(f_name);

		if (launch_f.exists()){
			launch_f.delete();
		}
		
		try {
			launch_f.createNewFile();

			BufferedWriter w = new BufferedWriter(new FileWriter(launch_f));
			w.write("@echo off");

			w.newLine();
			
			String af = Constants.app_dir + "/" + app + "/Applications_" + app + ".xml";
			String pf = Constants.app_dir + "/" + app + "/Platforms_" + app + ".xml";
			
			w.write("python " + Constants.dalo_exe +
					" -p " + pf             +
					" -a " + af             +
					" -P " + platform       +
					" -m kill");
			
			w.newLine();
			
			w.flush();			
			w.close();

		}
		catch (Exception e) {
			System.err.println("Error writing temporary file - check your permissions");
			System.err.println("Error: " + e.getMessage());
		}
		
		Tools.chmod("+x", f_name);
		
		if (launch_f.canRead()) {
			return f_name;
		}
		else {
			System.err.println("Error generating an executable file!");
			return "";
		}
	}

	
	/** ************************************************************************
	 * generates a temporary shell script that launches TSAR
	 * @param app
	 * @param platform
	 * @param scenario
	 */
	private String generate_scenario_launcher(String app,
			                                   String platform,
			                                   String scenario) {
		
		String f_name = cnf.get("tmp_dir") + "/" + LAUNCH_TSAR + this.hashCode() + ".bat";
		
		File launch_f = new File(f_name);

		if (launch_f.exists()){
			launch_f.delete();
		}
		
		try {
			launch_f.createNewFile();
			


			BufferedWriter w = new BufferedWriter(new FileWriter(launch_f));
			w.write("@echo off");

			w.newLine();
			
			
			w.write(Constants.tsar_exe + " " + platform + " " + scenario);
			w.newLine();
			
			w.newLine();
			
			w.flush();			
			w.close();

		}
		catch (Exception e) {
			System.err.println("Error writing temporary file - check your permissions");
			System.err.println("Error: " + e.getMessage());
		}
		
		Tools.chmod("+x", f_name);
		
		if (launch_f.canRead()) {
			return f_name;
		}
		else {
			System.err.println("Error generating an executable file!");
			return "";
		}	
	}



}

package com.airbus.vibe.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;


public class Pinger extends Thread {
	
	private static final String PING_SIMU   = "ping_simu";
	
	private TsarGUI     gui;
	private String      script;
	
	/** ************************************************************************
	 * Constructor!
	 * @param g the associated gui
	 */
	public Pinger(TsarGUI g) {

		this.gui  = g;
		this.script = generateScript();
	}

	/** ************************************************************************
	 * Loops until the thread is interrupted. At each iteration it launches a
	 * new external  
	 */
    public void run()
    {

    	while(true) {
    		
    		
    		try {
    			Thread.sleep(1000);

    			if( ping() ) {
    				gui.getShell().getDisplay().
    				     asyncExec(new StatusUpdater(true, gui));
    			}
    			else {
    				gui.getShell().getDisplay().
				     asyncExec(new StatusUpdater(false, gui));
    			}
	
    		}

    		catch (IOException e) {
    			System.err.println(e.getMessage());
    			Tools.removeTmpFile(this.script);
    		}

    		catch (InterruptedException e) {
    			System.err.println("EOF");
    			Tools.removeTmpFile(this.script);
    			return;
    		}
    	}
    }
    
    public boolean ping() throws InterruptedException, IOException {
    	boolean online;
    	
    	// create process
    	Process p  = Runtime.getRuntime().exec(this.script);
    	p.waitFor();		

    	//handle output first
    	InputStreamReader stdout_isr = new InputStreamReader(p.getInputStream());
    	BufferedReader stdout_buffReader = new BufferedReader(stdout_isr);  	

    	String output_l;
    	// look for the first valid non empty line
        while( (output_l = stdout_buffReader.readLine()) != null ) {
        	if ( ! output_l.trim().equals(""))      	
        		break;
        }
        
        try {
	        // if the returned number is 0 (zero chars);
	        // there is a big big chance we are OFFLINE
	        String firstToken = output_l.split(",")[0];
	    	if ( firstToken.equals(Constants.cm_exe_name) ) {
				online = true;
			}
	    	else {
	    		// anything else would mean there is at least some 
	    		// dss or d2b process going on
	    		online = false;
	    	}
	    }
    	catch (NullPointerException e) {
    		// most likely the ping command failed because TOP or tasklist
    		// are not installed or accessible. fuck it.
    		online = false;
    	}
    	
    	p.destroy();
    	
    	return online;
    }
    
	/** ************************************************************************
	 * Writes a ksh script that detects VIBE simulations ongoing
	 * @return
	 */
	private String generateScript() {
		
		Config cnf = Config.getConfig();

		final String cmd = Constants.ping_cmd;
		String f_name = cnf.get("tmp_dir") + "/" + PING_SIMU + this.hashCode() + ".bat";

		File launch_f = new File(f_name);

		if (launch_f.exists()){
			launch_f.delete();
		}

		try {
			launch_f.createNewFile();

			BufferedWriter w = new BufferedWriter(new FileWriter(launch_f));
			w.write("@echo off");
			w.newLine();
			w.write(cmd);
			w.newLine();
			w.flush();			
			w.close();
		}
		catch (Exception e) {
			System.err.println("Error writing temporary file -" +
					" check your permissions");
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

	public String getScript() {
		return script;
	}

    
}

//******************************************************************************
//******************************************************************************

/** ****************************************************************************
 * A runnable class that  updates the GUI according to the VIBE simu status
 * @author saflores
 *
 */
class StatusUpdater implements Runnable {

	String line;
	Label lblStatus;
	Label lblSimuIcon;
	boolean online;

	public StatusUpdater(boolean online, TsarGUI g) {
		this.lblStatus = g.getLblStatus();
		this.lblSimuIcon = g.getLblSimuIcon();
		this.online = online;
	}
	
	public void run() {
		if (lblStatus.isDisposed())
			return;
		if (lblSimuIcon.isDisposed())
			return;
		
		if (online) {
			lblStatus.setText("VIBE simulation is online");
			lblSimuIcon.setImage(SWTResourceManager.getImage(TsarGUI.class,
					"/com/airbus/vibe/gui/icons/network-online.png"));
		}
		else {
			lblStatus.setText("VIBE simulation is offline");
			lblSimuIcon.setImage(SWTResourceManager.getImage(TsarGUI.class,
					"/com/airbus/vibe/gui/icons/network-offline.png"));
		}
		lblStatus.redraw();
		lblSimuIcon.redraw();
		lblStatus.getDisplay().wake();
	}
	
}


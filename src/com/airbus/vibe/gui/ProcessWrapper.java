package com.airbus.vibe.gui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.eclipse.swt.custom.StyledText;


public class ProcessWrapper extends Thread {
	
	String      cmd;
	StyledText  text;
	TsarGUI     gui;

	
	public ProcessWrapper(String cmd, StyledText text, TsarGUI g) {
		this.cmd  = cmd;
		this.text = text;
		this.gui  = g;
		// this allows the parent GUI thread to die peacefully without waiting
		// for this thread to die. Say no to Zombies.
		this.setDaemon(true);
	}

    public void run()
    {
    	
    	
        try {
        	// create process
        	Process p  = Runtime.getRuntime().exec(this.cmd);
            
        	//handle output first
        	InputStreamReader stdout_isr = new InputStreamReader(p.getInputStream());
            BufferedReader stdout_buffReader = new BufferedReader(stdout_isr);
            String out_l = "";

            while( (out_l = stdout_buffReader.readLine()) != null ) {            	
            		gui.getShell().getDisplay().asyncExec(
            											new TextUpdater(out_l,
            													 this.text)
            										  );
            }

            // handle errors at the end
            InputStreamReader stderr_isr = new InputStreamReader(p.getErrorStream());
            BufferedReader stderr_buffReader = new BufferedReader(stderr_isr);
            String err_l = "";
            
            while( (err_l = stderr_buffReader.readLine()) != null ) {            	
        		gui.getShell().getDisplay().asyncExec(
						new TextUpdater(err_l,
								 this.text)
					  );
            }
            
            p.waitFor();
        }
        catch (IOException ioe) {
        	ioe.printStackTrace();  
        }
        catch (InterruptedException e) {
        	System.err.println("interrupted");
        	return;
		}		
    }
}

class TextUpdater implements Runnable {

	String line;
	StyledText text;

	public TextUpdater(String l, StyledText t) {
		if (l.length() > 250) { 
			this.line = l.substring(0, 250)+"\n";
		}
		else{
			this.line = l+"\n";
		}
	
		this.text = t; 
	}
	
	public void run() {
		if (this.text.isDisposed())
			return;
		
		
		this.text.append(this.line);
		this.text.redraw();
		this.text.getDisplay().wake();
	}
	
}



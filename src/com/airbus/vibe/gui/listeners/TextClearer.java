package com.airbus.vibe.gui.listeners;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TextClearer implements Listener {
	StyledText text;
	
	public TextClearer(StyledText t) {
		this.text = t;
	}
	
	public void handleEvent(Event event) {
		this.text.setText("");
	}
	
}


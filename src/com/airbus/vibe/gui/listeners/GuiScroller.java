
package com.airbus.vibe.gui.listeners;


import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

//####################
//#### a listener to scroll on user input

public class GuiScroller implements Listener {
	StyledText text;
	int lastIndex;
	
	public GuiScroller(StyledText t) {
		this.text = t;
		this.lastIndex = text.getTopIndex();
	}
	
	public void handleEvent(Event event) {
		int index = this.text.getTopIndex();
		if (index != this.lastIndex) {
			this.lastIndex = index;
		}
	}
}
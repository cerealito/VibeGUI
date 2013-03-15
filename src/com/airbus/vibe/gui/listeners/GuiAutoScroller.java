package com.airbus.vibe.gui.listeners;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;


//#### #### a listener to "follow" the text on output (auto scrolling)
public class GuiAutoScroller implements ModifyListener {
	StyledText text;
	public GuiAutoScroller(StyledText t) {
		this.text = t;
	}

	// go to the end upon arrival of new text
	public void modifyText(ModifyEvent e) {
		this.text.setSelection(this.text.getCharCount());
	}
}

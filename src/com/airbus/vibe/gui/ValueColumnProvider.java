package com.airbus.vibe.gui;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public class ValueColumnProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		//cast input element to desired class...
		// return somthing form the class
		
		return "50ms";
		
	}
}

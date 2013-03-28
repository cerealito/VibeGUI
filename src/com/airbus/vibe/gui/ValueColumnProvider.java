package com.airbus.vibe.gui;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.airbus.vibe.dalo.SimItemWrapper;

public class ValueColumnProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		//cast input element to desired class...
		if (element instanceof SimItemWrapper) {
			return ((SimItemWrapper)element).version;
		}
		else {
			return null;
		}
	}
}

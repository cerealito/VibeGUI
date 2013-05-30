package com.airbus.vibe.dalo;

import mx.bitch.util.Tree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;
import org.w3c.dom.Node;

import com.airbus.vibe.gui.TsarGUI;

public class SWTLabelProvider extends StyledCellLabelProvider {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 * 
	 * This is the part that creates the contents of the viewer "cell"
	 * 
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		//we need a styled string to add italics and icons and so on
		StyledString text = new StyledString();
		
		if (element instanceof Tree<?>) {
			NodeWrapper nw = ((Tree<NodeWrapper>)element).getElement();
			//put the actor name in every case
			text.append(nw.actorName());
			
			// add an icon for known linux hosts or windows pcs
			if(nw.isComputer()) {
				if (nw.actorName().equalsIgnoreCase("linuxhost")) {
					cell.setImage(SWTResourceManager.getImage(TsarGUI.class,
			                   "/com/airbus/vibe/gui/icons/network-server.png"));
				}
				else {
					cell.setImage(SWTResourceManager.getImage(TsarGUI.class,
					                 "/com/airbus/vibe/gui/icons/computer.png"));
				}
			}

			// If there are CPIOMs add the type in italics
			if (nw.type().equals("CPIOM")) {

				// this is quite mysterious to me: we get an array of FontData objects from the current "cell"
				final FontData[] italicFontData = getModifiedFontData(cell.getFont().getFontData(), SWT.ITALIC);

				text.append("     [" + nw.type() + "] ", new Styler() {

					public void applyStyles(TextStyle textStyle) {
						// this is another mysterious thing: we create a (single) font
						// out of the Display and the modified FontData array  and we apply it to the text
						// we appended. go figure.
						Font italicFont = new Font(Display.getCurrent(), italicFontData);
						textStyle.font = italicFont;
					}
				});
			}
		} 

		// in every case :
		
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		super.update(cell);
	}

	/*
	 * This method makes a deep, modified copy of an array of FontData objects
	 * The modification takes place in the individual constructors of the FontData objects:
	 * the passed additional style int is added to original.
	 */
	private static FontData[] getModifiedFontData(FontData[] original, int additionalStyle) {
		FontData[] modFontData = new FontData[original.length];
		for(int i = 0; i < modFontData.length; i++) {
			FontData iBase = original[i];
			modFontData[i] = new FontData(iBase.getName(), iBase.getHeight(), iBase.getStyle() | additionalStyle);
		}
		return modFontData;
	}
	
}

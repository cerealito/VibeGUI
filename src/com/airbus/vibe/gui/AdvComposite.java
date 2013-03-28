package com.airbus.vibe.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.wb.swt.SWTResourceManager;

import com.airbus.vibe.dalo.ActorsFileReader;
import com.airbus.vibe.dalo.SWTLabelProvider;
import com.airbus.vibe.dalo.SWTTreeProvider;

import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;

public class AdvComposite extends Composite {

	private CheckboxTreeViewer  advTreeViewer;

	private SashForm advSash;
	
	private Label  advLblSelectComponents;
	private Table  advPropTable;

	private TableViewer advPropTableViewer;
	
	private Label  advLblInfo1;
	private Label  advLblCurrentFile;
	private String advStrCurrentActorsFile;
	
	
	private ActorsFileReader actorsReader;
	
	// getters 
	public Table getAdvPropTable() {
		return advPropTable;
	}

	public TableViewer getAdvPropTableViewer() {
		return advPropTableViewer;
	}

	public Label getAdvLblInfo1() {
		return advLblInfo1;
	}

	public Label getAdvLblCurrentFile() {
		return advLblCurrentFile;
	}
	
	public Label getAdvLblSelectComponents() {
		return advLblSelectComponents;
	}

	public CheckboxTreeViewer getAdvTreeViewer() {
		return advTreeViewer;
	}
	
	public String getAdvStrCurrentActorsFile() {
		return advStrCurrentActorsFile;
	}
	
	public void setAdvStrCurrentActorsFile(String s) {
		this.advStrCurrentActorsFile = s;
	}
	
	public ActorsFileReader getActorsReader() {
		return actorsReader;
	}

	public void setActorsReader(ActorsFileReader actorsReader) {
		System.out.println("setting actorsReader to " + actorsReader.hashCode());
		this.actorsReader = actorsReader;
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AdvComposite(Composite parent, int style) {
		super(parent, style);

		this.setLayout(new GridLayout(1, true));
		
		advLblInfo1 = new Label(this, SWT.NONE);
		advLblInfo1.setEnabled(false);
		advLblInfo1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		advLblInfo1.setText("Select an application description...");
		
		advLblCurrentFile = new Label(this, SWT.NONE);
		advLblCurrentFile.setEnabled(false);
		advLblCurrentFile.setForeground(SWTResourceManager.getColor(0, 51, 255));
		
		advLblCurrentFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		advLblSelectComponents = new Label(this, SWT.NONE);
		advLblSelectComponents.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		advLblSelectComponents.setText("Select components  to launch:");
		

		advSash = new SashForm(this,SWT.NONE);
		advSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		// ######### the fucking TREE
		advTreeViewer = new CheckboxTreeViewer(advSash);
		Tree tree = advTreeViewer.getTree();
		tree.setEnabled(false);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		advTreeViewer.setContentProvider(new SWTTreeProvider());
		advTreeViewer.setLabelProvider(new SWTLabelProvider());

		// ######### the new table :D
		advPropTableViewer = new TableViewer(advSash, SWT.BORDER | SWT.FULL_SELECTION);
		advPropTable = advPropTableViewer.getTable();
		GridData gd_advPropTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_advPropTable.widthHint = 100;
		advPropTable.setLayoutData(gd_advPropTable);
		advPropTable.setHeaderVisible(true);
		advPropTable.setLinesVisible(true);

		advPropTableViewer.setContentProvider(ArrayContentProvider.getInstance());

		// #### first column with attribute label
		TableViewerColumn advPropColAttrib = new TableViewerColumn(advPropTableViewer, SWT.NONE);
		advPropColAttrib.getColumn().setWidth(100);
		advPropColAttrib.getColumn().setText("Name");

		advPropColAttrib.setLabelProvider(new AttributeColumnProvider());

		// #### second column with the actual value of each attribute
		TableViewerColumn advPropColValue = new TableViewerColumn(advPropTableViewer, SWT.NONE);
		advPropColValue.getColumn().setWidth(100);
		advPropColValue.getColumn().setText("Version");

		advPropColValue.setLabelProvider(new ValueColumnProvider());
				
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

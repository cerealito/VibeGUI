package com.airbus.vibe.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.wb.swt.SWTResourceManager;

import com.airbus.vibe.dalo.SWTLabelProvider;
import com.airbus.vibe.dalo.SWTTreeProvider;

import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;

public class AdvComposite extends Composite {

	private CheckboxTreeViewer  advTreeViewer;

	private Label  advLblSelectComponents;
	private Table  advPropTable;

	private TableViewer advPropTableViewer;
	
	private Label  advLblInfo1;
	private Label  advLblCurrentFile;
	
	// getters and setters
	public Table getAdvPropTable() {
		return advPropTable;
	}

	public void setAdvPropTable(Table advPropTable) {
		this.advPropTable = advPropTable;
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
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AdvComposite(Composite parent, int style) {
		super(parent, style);

		this.setLayout(new GridLayout(2, false));
		
		advLblInfo1 = new Label(this, SWT.NONE);
		advLblInfo1.setEnabled(false);
		advLblInfo1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		advLblInfo1.setText("Select an application description...");
		new Label(this, SWT.NONE);
		
		advLblCurrentFile = new Label(this, SWT.NONE);
		advLblCurrentFile.setEnabled(false);
		advLblCurrentFile.setForeground(SWTResourceManager.getColor(0, 51, 255));
		
		advLblCurrentFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);
		

		
		
		advLblSelectComponents = new Label(this, SWT.NONE);
		advLblSelectComponents.setText("Select components  to launch:");
		new Label(this, SWT.NONE);



		// ######### the fucking TREE
		advTreeViewer = new CheckboxTreeViewer(this);
		Tree tree = advTreeViewer.getTree();
		tree.setEnabled(false);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		advTreeViewer.setContentProvider(new SWTTreeProvider());
		advTreeViewer.setLabelProvider(new SWTLabelProvider());

		advPropTableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		advPropTable = advPropTableViewer.getTable();
		advPropTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));


	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

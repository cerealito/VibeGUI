package com.airbus.vibe.gui;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.SWTResourceManager;

import com.airbus.vibe.dalo.SWTLabelProvider;
import com.airbus.vibe.dalo.SWTTreeProvider;
import com.airbus.vibe.gui.listeners.AdvLblCurrentFileSetter;
import com.airbus.vibe.gui.listeners.AdvModeActivator;
import com.airbus.vibe.gui.listeners.AdvTreeViewerChecker;
import com.airbus.vibe.gui.listeners.ComboAppFiller;
import com.airbus.vibe.gui.listeners.ComboPlatFiller;
import com.airbus.vibe.gui.listeners.ComboScenarioAction;
import com.airbus.vibe.gui.listeners.EditorOpener;
import com.airbus.vibe.gui.listeners.EditorRefresher;
import com.airbus.vibe.gui.listeners.GuiAutoScroller;
import com.airbus.vibe.gui.listeners.GuiScroller;
import com.airbus.vibe.gui.listeners.LaunchButtonEnabler;
import com.airbus.vibe.gui.listeners.OnlineDetector;
import com.airbus.vibe.gui.listeners.TextClearer;
import com.airbus.vibe.gui.listeners.btnBrowseAction;
import com.airbus.vibe.gui.listeners.btnFireAction;
import com.airbus.vibe.gui.listeners.btnKillAction;
import com.airbus.vibe.gui.listeners.btnLaunchAction;


public class TsarGUI {
	
	//#########################################################################
	// General variables
	
	private boolean online;
	private Display display;
	private Shell   shell;

	private Controller controller;
	private TabFolder  mainFolder;
	private Composite  maincontainer;
	
	//#########################################################################
	// Variables of the Control Tab	
	private TabItem tabSimu;
	
	private Composite simuComposite;
	private Button btnLaunch;
	private Button btnKill;

	private Button     btnFire;

	private Combo comboApp;
	private Combo comboPlatform;

	private Button checkBoxAdvMode;
	
	private TabFolder  simuFolder;

		//##################################################################
		// Variables of the output SubTab
		private TabItem    tabOutput;
		private Composite  outputComposite;
		private StyledText simuText;

		//##################################################################
		// Variables of the advanced subTab			
		private TabItem tabAdvanced;

			private AdvComposite advancedComposite;
			
			private CheckboxTreeViewer  advTreeViewer;
			private TableViewer advPropTableViewer;
			private Table advPropTable;

			
			private Label  advLblSelectComponents;
			
			
			private Label  advLblInfo1;
	
	
			private Label  advLblCurrentFile;
	
	//#########################################################################
	// Variables of the Scenario Tab
	private TabItem   tabScenario;
	private Composite scenarioComposite;		
	
	private Combo      comboScenario;
	private Button     btnBrowse;	

	private TabFolder scenarioFolder;
		//##################################################################
		// Variables of the edition subTab
		private TabItem    tabEdition;
		private Composite  editComposite;
		private ToolItem   tItemEdit;
		private ToolItem   tItemRefresh;
		private StyledText editorText;
		
		//##################################################################
		// Variables of the tsar execution subTab
		private TabItem    tabExec;
		private Composite  execComposite;
		private StyledText tsarText;	

	//#########################################################################
	// Variables of the Status bar		
	private Composite statusBarCst;
	private Label     lblStatus;
	private Label     lblSimuIcon;

	private Label lblErrors;
	

	
	//##########################################################################
	// Getters and Setters
	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public Shell getShell() {
		return shell;
	}

	public Controller getController() {
		return controller;
	}

	public StyledText getSimuText() {
		return simuText;
	}

	public StyledText getTsarText() {
		return tsarText;
	}
	
	public StyledText getEditorText() {
		return editorText;
	}

	public Label getLblStatus() {
		return lblStatus;
	}
	
	public Label getLblSimuIcon() {
		return lblSimuIcon;
	}

	public Button getBtnLaunch() {
		return btnLaunch;
	}

	public Button getCheckBoxAdvMode() {
		return checkBoxAdvMode;
	}

	public Button getBtnKill() {
		return btnKill;
	}
	
	public Button getBtnFire() {
		return btnFire;
	}

	public Button getBtnAdvancedMode() {
		return checkBoxAdvMode;
	}

	public TabFolder getSimuFolder() {
		return simuFolder;
	}

	public TabItem getTabAdvanced() {
		return tabAdvanced;
	}

	public TabItem getTabExec() {
		return tabExec;
	}

	public TabItem getTabOutput() {
		return tabOutput;
	}

	public TabFolder getScenarioFolder() {
		return scenarioFolder;
	}

	public Combo getComboScenario() {
		return comboScenario;
	}

	public Combo getComboApp() {
		return comboApp;
	}

	public Combo getComboPlatform() {
		return comboPlatform;
	}
	
	public CheckboxTreeViewer  getAdvTreeViewer() {
		//return advTreeViewer;
		return advancedComposite.getAdvTreeViewer();
	}
	
	public Label getAdvLblInfo1() {
		//return advLblInfo1;
		return advancedComposite.getAdvLblInfo1();
	}

	public Label getAdvLblSelectComponents() {
		//return advLblSelectComponents;
		return advancedComposite.getAdvLblSelectComponents();
	}

	public Label getAdvLblCurrentPlatform() {
		//return advLblCurrentFile;
		return advancedComposite.getAdvLblCurrentFile();
	}

	//##########################################################################
	/**
	 * Constructor
	 */
	public TsarGUI(Controller c) {
		this.display = new Display();
		this.shell = new Shell(display, SWT.SHELL_TRIM | SWT.BORDER); 
				                                   
		shell.setSize(750, 650);
		this.configureShell();
		this.createContents(this.shell);
		

		this.controller = c;
	}

	//##########################################################################
	/**
	 * Configure the shell.
	 * @param newShell
	 */
	protected void configureShell() {
		this.shell.setMinimumSize(new Point(750, 650));
		this.shell.setImage(SWTResourceManager.getImage(TsarGUI.class, "/com/airbus/vibe/gui/icons/suse-action-games.png"));
		this.shell.setText("VIBE Simulation Launcher");
		this.shell.pack();
	}

	//##########################################################################
	/**
	 * Main loop
	 * 
	 */
	public void open() {

		this.shell.open();

		while (!this.shell.isDisposed()) {
			if (!this.display.readAndDispatch()) {
				this.display.sleep();
				//System.out.println("put to sleep");
			}
			//System.out.println("awaked");
		}
		this.shell.dispose();
	}

	//##########################################################################
	/**
	 * Create contents of the application window.
	 * @param parent
	 */

	protected Control createContents(Composite parent) {
		GridLayout gl_shell = new GridLayout(1, false);
		shell.setLayout(gl_shell);

		maincontainer = new Composite(parent, SWT.NONE);
		GridData gd_maincontainer = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_maincontainer.widthHint = 700;
		maincontainer.setLayoutData(gd_maincontainer);
		maincontainer.setFont(SWTResourceManager.getFont("Sans", 10, SWT.NORMAL));
		
		statusBarCst = new Composite(parent, SWT.NONE);
		statusBarCst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_statusBarCst = new GridLayout(3, false);
		gl_statusBarCst.horizontalSpacing = 10;
		gl_statusBarCst.verticalSpacing = 0;
		gl_statusBarCst.marginWidth = 0;
		gl_statusBarCst.marginHeight = 0;
		statusBarCst.setLayout(gl_statusBarCst);
		
		lblErrors = new Label(statusBarCst, SWT.NONE);
		lblErrors.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblErrors.setFont(SWTResourceManager.getFont("Sans", 8, SWT.BOLD));
		lblErrors.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		
		this.lblStatus = new Label(statusBarCst, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblStatus.setAlignment(SWT.RIGHT);
		lblStatus.setImage(null);
		lblStatus.setText("VIBE Simulation is offline");		
		
		
		
		lblSimuIcon = new Label(statusBarCst, SWT.NONE);
		lblSimuIcon.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSimuIcon.setImage(SWTResourceManager.getImage(TsarGUI.class, "/com/airbus/vibe/gui/icons/network-offline.png"));
		GridLayout gl_maincontainer = new GridLayout(1, false);
		gl_maincontainer.marginHeight = 0;
		gl_maincontainer.marginWidth = 0;
		maincontainer.setLayout(gl_maincontainer);

		//######################################################################
		//######################################################################
		//######################################################################
		//####                    THE TABBED INTERFACE                      #### 
		//######################################################################
		//######################################################################
		//######################################################################
		
		
		mainFolder = new TabFolder(maincontainer, SWT.NONE);
		mainFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		//######################################################################
		//######################################################################
		//####                    FIRST Tab: simu                           ####
		//######################################################################
		//######################################################################
		
		tabSimu = new TabItem(mainFolder, SWT.NONE);
		tabSimu.setText("Simulation Control");

		simuComposite = new Composite(mainFolder, SWT.NONE);
		tabSimu.setControl(simuComposite);
		simuComposite.setLayout(new GridLayout(4, false));
		
		//######### labels
		Label lblApplication = new Label(simuComposite, SWT.NONE);
		lblApplication.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		lblApplication.setText("Application");
		
		Label lblPlatform = new Label(simuComposite, SWT.NONE);
		lblPlatform.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		lblPlatform.setText("Platform");

		new Label(simuComposite, SWT.NONE);
		new Label(simuComposite, SWT.NONE);

		//######### combo app & combo platform
		comboApp = new Combo(simuComposite, SWT.READ_ONLY);
		GridData gd_comboApp = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboApp.heightHint = 29;
		gd_comboApp.widthHint = 132;
		comboApp.setLayoutData(gd_comboApp);

		comboPlatform = new Combo(simuComposite, SWT.READ_ONLY);
		GridData gd_comboPlatform = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboPlatform.widthHint = 155;
		comboPlatform.setLayoutData(gd_comboPlatform);
		
		
		
		new Label(simuComposite, SWT.NONE);
		new Label(simuComposite, SWT.NONE);
		
		checkBoxAdvMode = new Button(simuComposite, SWT.CHECK);
		checkBoxAdvMode.setText("Advanced Mode");
		new Label(simuComposite, SWT.NONE);



		//######### Button Launch 
		btnLaunch = new Button(simuComposite, SWT.NONE);
		GridData gd_btnLaunch = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnLaunch.heightHint = 34;
		gd_btnLaunch.widthHint = 152;
		btnLaunch.setLayoutData(gd_btnLaunch);
		btnLaunch.setEnabled(false);
		btnLaunch.setImage(SWTResourceManager.getImage(TsarGUI.class,
				"/com/airbus/vibe/gui/icons/suse-action-games.png"));
		btnLaunch.setText("Launch simulation");



		//######### Button kill
		btnKill = new Button(simuComposite, SWT.NONE);
		GridData gd_btnKill = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnKill.heightHint = 34;
		gd_btnKill.widthHint = 118;
		btnKill.setLayoutData(gd_btnKill);

		//we can invoke kill at any time, including from the very start
		btnKill.setEnabled(true);
		btnKill.setImage(SWTResourceManager.getImage(TsarGUI.class,
				"/com/airbus/vibe/gui/icons/process-stop.png"));
		btnKill.setText("Kill Simulation");


		//######################################################################
		//####            SIMU FOLDER: output and advanced TAB              ####
		//######################################################################
		simuFolder = new TabFolder(simuComposite, SWT.NONE);
		GridData gd_controlFolder = new GridData(SWT.FILL, SWT.FILL, false, true, 4, 1);
		gd_controlFolder.heightHint = 455;
		simuFolder.setLayoutData(gd_controlFolder);
		
		//######################################################################
		//#### #### Output SubTAB
		tabOutput = new TabItem(simuFolder, SWT.NONE);
		tabOutput.setText("Output");
		
		outputComposite = new Composite(simuFolder, SWT.NONE);
		tabOutput.setControl(outputComposite);
		outputComposite.setLayout(new GridLayout(1, false));
		
		//#### #### #### The simu text viewer & text within		
		TextViewer simuTextViewer = new TextViewer(outputComposite, SWT.BORDER   | 
                                                               SWT.READ_ONLY|
                                                               SWT.V_SCROLL |
                                                               SWT.H_SCROLL  );

		simuText = simuTextViewer.getTextWidget();
		simuText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		simuText.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));

		simuText.setFont(SWTResourceManager.getFont("Bitstream Vera Sans Mono",
				                                    9,
				                                    SWT.NORMAL)
				        );
		
		simuText.setEditable(false);

		//#### #### vertical and horizontal scrollbars & listeners		

		GuiScroller simu_scroll = new GuiScroller(simuText);
		ScrollBar   vBarSimu;  
		ScrollBar   hBarSimu;
		// user scrolling
		if ((vBarSimu = simuText.getVerticalBar()) != null) {
			vBarSimu.addListener(SWT.Selection, simu_scroll);
		}
		if ( (hBarSimu = simuText.getHorizontalBar()) != null) {
			hBarSimu.addListener(SWT.Selection, simu_scroll);
		}
		// auto scrolling
		simuText.addModifyListener(new GuiAutoScroller(simuText));

		//######################################################################
		//#### #### Advanced SubTAB
		tabAdvanced = new TabItem(simuFolder, SWT.NONE);
		tabAdvanced.setText("Advanced");
			
		advancedComposite = new AdvComposite(simuFolder, SWT.NONE);
		tabAdvanced.setControl(advancedComposite);
		
//		advancedComposite.setLayout(new GridLayout(2, false));
//		
//		advLblInfo1 = new Label(advancedComposite, SWT.NONE);
//		advLblInfo1.setEnabled(false);
//		advLblInfo1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
//		advLblInfo1.setText("Select an application description...");
//		new Label(advancedComposite, SWT.NONE);
//		
//		advLblCurrentFile = new Label(advancedComposite, SWT.NONE);
//		advLblCurrentFile.setEnabled(false);
//		advLblCurrentFile.setForeground(SWTResourceManager.getColor(0, 51, 255));
//		
//		advLblCurrentFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		new Label(advancedComposite, SWT.NONE);
//
//		
//		
//		advLblSelectComponents = new Label(advancedComposite, SWT.NONE);
//		advLblSelectComponents.setText("Select components  to launch:");
//		new Label(advancedComposite, SWT.NONE);
//
//		// ######### the fucking TREE
//		advTreeViewer = new CheckboxTreeViewer(advancedComposite);
//		Tree tree = advTreeViewer.getTree();
//		tree.setEnabled(false);
//		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
//				
//		advTreeViewer.setContentProvider(new SWTTreeProvider());
//		advTreeViewer.setLabelProvider(new SWTLabelProvider());
//					
//		advPropTableViewer = new TableViewer(advancedComposite, SWT.BORDER | SWT.FULL_SELECTION);
//		advPropTable = advPropTableViewer.getTable();
//		advPropTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//######################################################################
		//######################################################################
		//####                    SECOND Tab: scenario                      ####
		//######################################################################
		//######################################################################
		tabScenario = new TabItem(mainFolder, SWT.NONE);
		tabScenario.setText("Scenario");
		scenarioComposite = new Composite(mainFolder, SWT.NONE);
		tabScenario.setControl(scenarioComposite);
		scenarioComposite.setLayout(new GridLayout(3, false));

		Label lblTsarScenario = new Label(scenarioComposite, SWT.NONE);
		lblTsarScenario.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblTsarScenario.setText("TSAR Scenario to launch:");
		
		new Label(scenarioComposite, SWT.NONE);
		new Label(scenarioComposite, SWT.NONE);

		comboScenario = new Combo(scenarioComposite, SWT.NONE);
		GridData gd_comboScenario = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboScenario.widthHint = 269;
		comboScenario.setLayoutData(gd_comboScenario);
				

		new Label(scenarioComposite, SWT.NONE);
		new Label(scenarioComposite, SWT.NONE);
		new Label(scenarioComposite, SWT.NONE);

		btnBrowse = new Button(scenarioComposite, SWT.NONE);
		GridData gd_btnBrowse = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowse.widthHint = 100;
		btnBrowse.setLayoutData(gd_btnBrowse);
		btnBrowse.setImage(SWTResourceManager.getImage(TsarGUI.class,
				"/com/airbus/vibe/gui/icons/document-open.png"));
		btnBrowse.setText("Browse");		

		btnFire = new Button(scenarioComposite, SWT.NONE);
		GridData gd_btnFire = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnFire.widthHint = 100;
		btnFire.setLayoutData(gd_btnFire);
		btnFire.setEnabled(false);
		btnFire.setImage(SWTResourceManager.getImage(TsarGUI.class,
				"/com/airbus/vibe/gui/icons/media-playback-start.png"));
		btnFire.setText("Fire");
		
		//######################################################################
		//####        SCENARIO FOLDER: edition and tsar execution TAB       ####
		//######################################################################
		scenarioFolder = new TabFolder(scenarioComposite, SWT.NONE);
		scenarioFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		//######################################################################
		//#### #### editon TAB
		tabEdition = new TabItem(scenarioFolder, SWT.NONE);
		tabEdition.setImage(SWTResourceManager.getImage(TsarGUI.class, "/com/airbus/vibe/gui/icons/format-justify-left.png"));
		tabEdition.setText("Edition");
		
		editComposite = new Composite(scenarioFolder, SWT.NONE);
		tabEdition.setControl(editComposite);
		editComposite.setLayout(new GridLayout(1, false));

		//#### #### Toolbar
		ToolBar toolBar = new ToolBar(editComposite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tItemEdit = new ToolItem(toolBar, SWT.NONE);
		tItemEdit.setImage(SWTResourceManager.getImage(TsarGUI.class, "/com/airbus/vibe/gui/icons/accessories-text-editor.png"));
		tItemEdit.setText("Open text editor");
		

	
		
		tItemRefresh = new ToolItem(toolBar, SWT.NONE);
		tItemRefresh.setImage(SWTResourceManager.getImage(TsarGUI.class, "/com/airbus/vibe/gui/icons/view-refresh.png"));
		tItemRefresh.setText("Refresh view");

		
		//#### #### The editor and text within		
		TextViewer editorTextViewer = new TextViewer(editComposite, SWT.BORDER| 
				                                                 SWT.READ_ONLY|
				                                                 SWT.V_SCROLL |
				                                                 SWT.H_SCROLL
				                                    );

		editorText = editorTextViewer.getTextWidget();
		editorText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		editorText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		editorText.setFont(SWTResourceManager.getFont("Bitstream Vera Sans Mono",
				                                    9,
				                                    SWT.NORMAL));


		//#### vertical and horizontal scrollbars & listeners		
		ScrollBar vBarEditor; 		 
		ScrollBar hBarEditor; 
		//user scroll
		GuiScroller editor_scroll = new GuiScroller(editorText); 		
		if ((vBarEditor = editorText.getVerticalBar()) != null) {
			vBarEditor.addListener(SWT.Selection, editor_scroll);
		}
		if ((hBarEditor = editorText.getHorizontalBar()) != null) {
			hBarEditor.addListener(SWT.Selection, editor_scroll);
		}
		// no auto scroll on edition view

		//######################################################################
		//#### #### execution TAB
		tabExec = new TabItem(scenarioFolder, SWT.NONE);
		tabExec.setImage(SWTResourceManager.getImage(TsarGUI.class, "/com/airbus/vibe/gui/icons/utilities-system-monitor.png"));
		tabExec.setText("TSAR Execution");

		execComposite = new Composite(scenarioFolder, SWT.NONE);
		tabExec.setControl(execComposite);
		execComposite.setLayout(new GridLayout(1, false));

		//#### #### The tsar text viewer & text within		
		TextViewer tsarTextViewer = new TextViewer(execComposite, SWT.BORDER   | 
				SWT.READ_ONLY|
				SWT.V_SCROLL |
				SWT.H_SCROLL
				);
		tsarText = tsarTextViewer.getTextWidget();
		tsarText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tsarText.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		tsarText.setFont(SWTResourceManager.getFont("Bitstream Vera Sans Mono",
				                                    9,
				                                    SWT.NORMAL));
		tsarText.setEditable(false);


		//#### vertical and horizontal scrollbars & listeners		
		GuiScroller tsar_scroll = new GuiScroller(tsarText); 
		ScrollBar   vBarTsar;
		ScrollBar   hBarTsar;
		// user scroll
		if ((vBarTsar = tsarText.getVerticalBar()) != null) {
			vBarTsar.addListener(SWT.Selection, tsar_scroll);
		}
		if ((hBarTsar = tsarText.getHorizontalBar()) != null) {
			hBarTsar.addListener(SWT.Selection, tsar_scroll);
		}
		// autoscroll
		tsarText.addModifyListener(new GuiAutoScroller(tsarText));

				
		tItemEdit.addListener(SWT.Selection, new EditorOpener(this));
		tItemRefresh.addListener(SWT.Selection, new EditorRefresher(this));

		
		
		Menu tsarTextMenu = new Menu(tsarText);
		MenuItem itmClearTsar = new MenuItem(tsarTextMenu, SWT.FLAT);
		itmClearTsar.setText("Clear");
		itmClearTsar.addListener(SWT.Selection, new TextClearer(tsarText));
				
		tsarText.setMenu(tsarTextMenu);
		
		Menu simuTextMenu = new Menu(simuText);
		MenuItem itmClearSimu = new MenuItem(simuTextMenu, SWT.FLAT);
		itmClearSimu.setText("Clear");
		itmClearSimu.addListener(SWT.Selection, new TextClearer(simuText));
				
		simuText.setMenu(simuTextMenu);

		//**********************************************************************
		//**********************************************************************
		//**********************************************************************
		//****                    TABBED INTERFACE ENDS                     **** 
		//**********************************************************************
		//**********************************************************************
		//**********************************************************************
		
		
		//######################################################################
		// listeners

		btnLaunch.addListener(SWT.Selection, new btnLaunchAction(this));
		
		btnKill.addListener(SWT.Selection, new btnKillAction(this));
	
		// better than activate? it should be called at least at the beginning
		// and ideally upon every click. For now it only works at the beginning.
		comboApp.addListener(SWT.Show, new ComboAppFiller(this));
		
		// CAREFUL!!! these guys MUST be in this order. 
		// Set the label first, then refill the comboPlatform
		comboApp.addListener(SWT.Selection, new AdvLblCurrentFileSetter(this));
		comboApp.addListener(SWT.Selection, new ComboPlatFiller(this));
		
		advancedComposite.getAdvTreeViewer().addCheckStateListener(new AdvTreeViewerChecker(this));
		
		// this guy is here twice
		AdvModeActivator myActivator = new AdvModeActivator(this);
		comboPlatform.addListener(SWT.Selection, myActivator);
		checkBoxAdvMode.addListener(SWT.Selection, myActivator);
		
		
		comboPlatform.addListener(SWT.Selection, new LaunchButtonEnabler(this));
		
		comboScenario.addModifyListener(new ComboScenarioAction(this));
		
		lblStatus.addPaintListener(new OnlineDetector(this));

		
		btnFire.addListener(SWT.Selection, new btnFireAction(this));

		btnBrowse.addListener(SWT.Selection, new btnBrowseAction(this));
		
		
		
		// set the combo app to something at the very beggining
		if (comboApp.getItemCount() > 0) {
			comboApp.select(comboApp.getItemCount()-1);
			comboApp.notifyListeners(SWT.Selection, new Event());
		}
		
		// ##################### GUI END ######################################
		//######################################################################
		
		return maincontainer;
	}
}

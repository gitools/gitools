package es.imim.bg.ztools.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix3D;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.TableView;
import es.imim.bg.ztools.ui.views.WelcomeView;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = -6899584212813749990L;

	private String appName;
	private String appVersion;
	
	private WorkspacePanel workspace;
	
	private StatusBar statusBar;
	
	private static AppFrame instance;
	
	public static AppFrame instance() {
		if (instance == null)
			instance = new AppFrame();
		return instance;
	}
	
	private AppFrame() {
		appName = getClass().getPackage().getImplementationTitle();
		if (appName == null)
			appName = "ztools";
		
		appVersion = getClass().getPackage().getImplementationVersion();
		if (appVersion == null)
			appVersion = "SNAPSHOT";
		
		createActions();
		createComponents();
		createWelcomeView();
		createDemoView();
		workspace.setSelectedIndex(0);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Options.instance().save();
				System.exit(0);
			}
		});

		setTitle(appName + " " + appVersion);
		setStatusText("Ok");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		pack();
	}

	public String getAppName() {
		return appName;
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	private void createActions() {
		Actions.openAnalysisAction.setEnabled(true);
	}
	
	private void createComponents() {
		setJMenuBar(createMenu());
		
		final JToolBar toolBar = createToolBar();
		
		workspace = new WorkspacePanel();
		
		statusBar = new StatusBar();
		
		add(toolBar, BorderLayout.NORTH);
		add(workspace, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}

	private JMenuBar createMenu() {
		
		// File
		
		final JMenu exportMenu = new JMenu("Export");
		exportMenu.add(Actions.exportParameterDataAction);
		exportMenu.add(Actions.exportColumnDataAction);
		exportMenu.add(Actions.exportNamesAction);
		
		final JMenu menuFile = new JMenu("File");
		menuFile.add(Actions.openAnalysisAction);
		menuFile.addSeparator();
		menuFile.add(Actions.closeAction);
		menuFile.addSeparator();
		menuFile.add(exportMenu);
		menuFile.addSeparator();
		menuFile.add(Actions.exitAction);
		
		// Selection
		
		final JMenu selectionModeMenu = new JMenu("Selection mode");
		selectionModeMenu.add(Actions.columnSelectionModeAction);
		selectionModeMenu.add(Actions.rowSelectionModeAction);
		selectionModeMenu.add(Actions.cellSelectionModeAction);
		
		final JMenu selMenu = new JMenu("Selection");
		selMenu.add(Actions.selectAllAction);
		//editMenu.add(Actions.invertSelectionAction);
		selMenu.add(Actions.unselectAllAction);
		selMenu.add(selectionModeMenu);
		selMenu.addSeparator();
		selMenu.add(Actions.hideSelectedColumnsAction);
		selMenu.add(Actions.hideSelectedRowsAction);
		selMenu.addSeparator();
		selMenu.add(Actions.moveRowsUpAction);
		selMenu.add(Actions.moveRowsDownAction);
		selMenu.add(Actions.moveColsLeftAction);
		selMenu.add(Actions.moveColsRightAction);
		selMenu.addSeparator();
		selMenu.add(Actions.sortSelectedColumnsAction);
		
		// Analysis
		
		final JMenu analysisMenu = new JMenu("Analysis");
		analysisMenu.add(Actions.zcalcAnalysisAction);
		
		// MTC
		 
		final JMenu mtcMenu = new JMenu("MTC");
		mtcMenu.add(Actions.mtcBonferroniAction);
		mtcMenu.add(Actions.mtcBenjaminiHochbergFdrAction);
		
		// Help
		
		final JMenu helpMenu = new JMenu("Help");
		helpMenu.add(Actions.aboutAction);
		
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(selMenu);
		menuBar.add(mtcMenu);
		menuBar.add(analysisMenu);
		menuBar.add(helpMenu);
		
		return menuBar;
	}
	
	private JToolBar createToolBar() {
		final JToolBar toolBar = new JToolBar();
		
		toolBar.add(Actions.openAnalysisAction).setText("");
		toolBar.add(Actions.closeAction).setText("");
		toolBar.addSeparator();
		toolBar.add(Actions.columnSelectionModeAction).setText("");
		toolBar.add(Actions.rowSelectionModeAction).setText("");
		toolBar.add(Actions.cellSelectionModeAction).setText("");
		toolBar.addSeparator();
		toolBar.add(Actions.selectAllAction).setText("");
		toolBar.add(Actions.unselectAllAction).setText("");
		toolBar.addSeparator();
		toolBar.add(Actions.hideSelectedColumnsAction).setText("");
		toolBar.add(Actions.hideSelectedRowsAction).setText("");
		toolBar.addSeparator();
		toolBar.add(Actions.moveColsLeftAction).setText("");
		toolBar.add(Actions.moveColsRightAction).setText("");
		toolBar.add(Actions.moveRowsUpAction).setText("");
		toolBar.add(Actions.moveRowsDownAction).setText("");
		toolBar.addSeparator();
		toolBar.add(Actions.sortSelectedColumnsAction).setText("");
		
		return toolBar;
	}
	
	private void createWelcomeView() {
		AbstractView view = new WelcomeView();
		workspace.addView(view);
	}
	
	private void createDemoView() {
		int rows = 40;
		int cols = 12;
		DoubleMatrix3D data = DoubleFactory3D.dense.random(2, rows, cols);
		
		final String[] rowNames = new String[data.rows()];
		for (int i = 0; i < rowNames.length; i++)
			rowNames[i] = "row " + (i + 1);
		
		final String[] colNames = new String[data.columns()];
		for (int i = 0; i < colNames.length; i++)
			colNames[i] = "col " + (i + 1);
		
		Results results = new Results(
				colNames, 
				rowNames, 
				new String[] {"right-p-value", "param2"}, 
				data);
		
		Analysis analysis = new Analysis();
		analysis.setResults(results);
		
		TableView view = 
			new TableView(
				new ResultsModel(results));
		view.setName("Demo");
		
		workspace.addView(view);
	}
	
	public void start() {
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	public WorkspacePanel getWorkspace() {
		return workspace;
	}
	
	public void setStatusText(String text) {
		statusBar.setText(text);
		repaint();
	}

	public ProgressMonitor createMonitor() {
		return statusBar.createMonitor();
	}

	public void refresh() {
		
	}
}


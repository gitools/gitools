package es.imim.bg.ztools.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix3D;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.ui.actions.OpenAnalysisAction;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.ResultsView;
import es.imim.bg.ztools.ui.views.View;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = -6899584212813749990L;

	private String appName;
	private String appVersion;
	
	private ActionManager am;
	
	private JTabbedPane workspace;
	
	private StatusBar statusBar;
	
	public AppFrame() {
		appName = getClass().getPackage().getImplementationTitle();
		if (appName == null)
			appName = "ztools";
		
		appVersion = getClass().getPackage().getImplementationVersion();
		if (appVersion == null)
			appVersion = "SNAPSHOT";
		
		createActions();
		createComponents();
		createDemoView();
		
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

	private void createActions() {
		am = new ActionManager();
		
		am.put(ActionManager.openAnalysisAction, 
				new OpenAnalysisAction(this));
	}
	
	private void createComponents() {
		setJMenuBar(createMenu());
		
		workspace = new JTabbedPane();
		
		statusBar = new StatusBar();
		
		add(workspace, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	private JMenuBar createMenu() {
		final JMenuItem menuiFileLoad = new JMenuItem(
				am.get(ActionManager.openAnalysisAction));
		
		final JMenu menuFile = new JMenu("File");
		menuFile.add(menuiFileLoad);
		
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		
		return menuBar;
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
		
		ResultsView view = new ResultsView(results);
		view.setName("demo");
		
		addWorkspaceView(view);
	}
	
	public void start() {
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	public void addWorkspaceView(AbstractView view) {
		final String name = view.getName() != null ? 
				view.getName() : "";
				
		final Icon icon = view.getIcon();
		
		if (icon == null)
			workspace.addTab(name, view);
		else
			workspace.addTab(name, icon, view);
		
		workspace.setSelectedComponent(view);
	}

	public AbstractView getWorkspaceCurrentView() {
		return (AbstractView) workspace.getSelectedComponent();
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

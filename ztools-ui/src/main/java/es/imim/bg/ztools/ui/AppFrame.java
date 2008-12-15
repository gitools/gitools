package es.imim.bg.ztools.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.ResultsMatrix;
import es.imim.bg.ztools.model.elements.ArrayElementFacade;
import es.imim.bg.ztools.model.elements.StringElementFacade;
import es.imim.bg.ztools.ui.actions.Actions;
import es.imim.bg.ztools.ui.jobs.JobProcessor;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.utils.IconUtils;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.DemoView;
import es.imim.bg.ztools.ui.views.WelcomeView;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = -6899584212813749990L;

	private String appName;
	private String appVersion;
	
	private WorkspacePanel workspace;
	
	private StatusBar statusBar;
	
	private JobProcessor jobProcessor;
	
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
		
		setIconImage(IconUtils.getImageIconResource(IconNames.logoMini).getImage());
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		pack();
		
		jobProcessor = new JobProcessor();
	}

	public String getAppName() {
		return appName;
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	private void createActions() {
		//FileActionSet.openAnalysisAction.setEnabled(true);
	}
	
	private void createComponents() {
		setJMenuBar(
				Actions.menuActionSet.createMenuBar());
		
		final JToolBar toolBar = 
			Actions.toolBarActionSet.createToolBar();
		
		workspace = new WorkspacePanel();
		
		statusBar = new StatusBar();
		
		add(toolBar, BorderLayout.NORTH);
		add(workspace, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	private void createWelcomeView() {
		AbstractView view = new WelcomeView();
		workspace.addView(view);
	}
	
	private void createDemoView() {
		int rows = 40;
		int cols = 12;
		
		int k = 0;
		DoubleMatrix1D values = DoubleFactory1D.dense.random(2 * rows * cols);
		
		ObjectMatrix2D data = ObjectFactory2D.dense.make(rows, cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				double[] v = new double[] {
					values.getQuick(k++),
					values.getQuick(k++)
				};
				data.setQuick(row, col, v);
			}
		}
		
		final ObjectMatrix1D rowNames = ObjectFactory1D.dense.make(data.rows());
		for (int i = 0; i < rowNames.size(); i++)
			rowNames.setQuick(i, "row " + (i + 1));
		
		final ObjectMatrix1D colNames = ObjectFactory1D.dense.make(data.columns());
		for (int i = 0; i < colNames.size(); i++)
			colNames.setQuick(i, "col " + (i + 1));
		
		ResultsMatrix resultsMatrix = new ResultsMatrix(
				rowNames,
				colNames,   
				data,
				new StringElementFacade(), 
				new StringElementFacade(),
				new ArrayElementFacade(new String[] {"param1", "param2"}));
		
		Analysis analysis = new Analysis();
		analysis.setResults(resultsMatrix);
		
		/*TableView view = 
			new TableView(
				new ResultsModel(results));*/
		
		AbstractView demoView = 
			new DemoView(new ResultsModel(resultsMatrix));
		
		demoView.setName("Demo");
		
		workspace.addView(demoView);
	}

	public void start() {
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	public WorkspacePanel getWorkspace() {
		return workspace;
	}
	
	public JobProcessor getJobProcessor() {
		return jobProcessor;
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


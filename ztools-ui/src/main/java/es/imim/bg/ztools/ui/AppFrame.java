package es.imim.bg.ztools.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.ui.actions.Actions;
import es.imim.bg.ztools.ui.jobs.JobProcessor;
import es.imim.bg.ztools.ui.utils.IconUtils;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.DemoView;
import es.imim.bg.ztools.ui.views.WelcomeView;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = -6899584212813749990L;

	private static String appName;
	private static String appVersion;
	
	static {
		appName = AppFrame.class.getPackage().getImplementationTitle();
		if (appName == null)
			appName = "GiTools";
		
		appVersion = AppFrame.class.getPackage().getImplementationVersion();
		if (appVersion == null)
			appVersion = "SNAPSHOT";
	}
	
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
		setPreferredSize(new Dimension(800, 680));
		pack();
		
		jobProcessor = new JobProcessor();
	}

	public static String getAppName() {
		return appName;
	}
	
	public static String getAppVersion() {
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
		AbstractView demoView = 
			new DemoView(40, 12);
		
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


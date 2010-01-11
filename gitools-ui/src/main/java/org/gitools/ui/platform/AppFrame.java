package org.gitools.ui.platform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import org.gitools.ui.IconNames;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.editor.html.WelcomeEditor;
import org.gitools.ui.heatmap.editor.HeatmapDemoEditor;
import org.gitools.ui.jobs.JobProcessor;
import org.gitools.ui.workspace.NavigatorPanel;
import org.gitools.ui.settings.Settings;
import org.gitools.workspace.Workspace;
import org.gitools.workspace.WorkspaceManager;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.view.details.DetailsView;
import org.gitools.ui.view.properties.PropertiesView;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = -6899584212813749990L;

	public enum WorkbenchLayout {
		LEFT, RIGHT, TOP, BOTTOM
	}
	
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
	
	private static final int defaultDividerLocation = 340;
	
	private WorkbenchLayout layout;
	
	private JToolBar toolBar;
	
	private JTabbedPane leftPanel;
	
	private NavigatorPanel navPanel;

	private DetailsView detailsView;

	private PropertiesView propertiesView;
	
	private EditorsPanel editorsPanel;
	
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
		
		this.layout = WorkbenchLayout.LEFT;
		createComponents();
		
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				Settings.getDefault().save();
				System.exit(0);
			}
		});

		setTitle(appName + " " + appVersion);
		setStatusText("Ok");
		
		setIconImage(IconUtils.getImageIconResource(IconNames.logoMini).getImage());
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 650));
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
		setJMenuBar(Actions.menuActionSet.createMenuBar());
		
		toolBar = Actions.toolBarActionSet.createToolBar();
		
		Workspace workspace = openWorkspace();
		navPanel = new NavigatorPanel(workspace);
				
		leftPanel = new JTabbedPane();
		//leftPanel.setTabPlacement(JTabbedPane.LEFT);
		//leftPanel.add(navPanel, "Navigator");

		/* Details view */
		detailsView = new DetailsView();
		leftPanel.add(detailsView, "Details");

		/* Properties view */
		propertiesView = new PropertiesView();
		leftPanel.add(propertiesView, "Properties");

		editorsPanel = new EditorsPanel();
		
		statusBar = new StatusBar();
		
		configureLayout(layout);
	}
	
	private void configureLayout(WorkbenchLayout layout) {
		int splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
		boolean leftOrTop = true;
		switch(layout) {
		case LEFT:
		case RIGHT:
			splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
			break;
		case TOP:
		case BOTTOM:
			splitOrientation = JSplitPane.VERTICAL_SPLIT;
			break;
		}
		switch(layout) {
		case LEFT:
		case TOP: leftOrTop = true; break;
		case RIGHT:
		case BOTTOM: leftOrTop = false; break;
		}
		
		final JSplitPane splitPane = new JSplitPane(splitOrientation);
		if (leftOrTop) {
			splitPane.add(leftPanel);
			splitPane.add(editorsPanel);
		}
		else {
			splitPane.add(editorsPanel);
			splitPane.add(leftPanel);
		}
		splitPane.setDividerLocation(defaultDividerLocation);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		
		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	private Workspace openWorkspace() {
		Workspace workspace = WorkspaceManager.getDefault().getWorkspace();
		
		return workspace;
	}
	
	private void createWelcomeView() {
		AbstractEditor view = new WelcomeEditor();
		editorsPanel.addEditor(view);
	}
	
	private void createDemoView() {
		AbstractEditor demoView = 
			new HeatmapDemoEditor(40, 12);
		
		editorsPanel.addEditor(demoView);
	}

	public void start() {
		createWelcomeView();
		createDemoView();
		editorsPanel.setSelectedIndex(0);
		
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	public NavigatorPanel getNavigatorPanel() {
		return navPanel;
	}
	
	public EditorsPanel getEditorsPanel() {
		return editorsPanel;
	}
	
	public JobProcessor getJobProcessor() {
		return jobProcessor;
	}

	public DetailsView getDetailsView() {
		return detailsView;
	}

	public PropertiesView getPropertiesView() {
		return propertiesView;
	}
	
	public void setStatusText(String text) {
		statusBar.setText(text);
		repaint();
	}

	public IProgressMonitor createMonitor() {
		return statusBar.createMonitor();
	}

	public void refresh() {
		
	}
}

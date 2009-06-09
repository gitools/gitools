package org.gitools.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import org.apache.velocity.app.VelocityEngine;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.editor.AbstractEditor;
import org.gitools.ui.editor.html.WelcomeEditor;
import org.gitools.ui.editor.table.DemoEditor;
import org.gitools.ui.jobs.JobProcessor;
import org.gitools.ui.panels.TemplatePane;
import org.gitools.ui.utils.IconUtils;
import org.gitools.ui.utils.Options;

import edu.upf.bg.progressmonitor.ProgressMonitor;

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
	
	private static final int defaultDividerLocation = 280;
	
	private WorkbenchLayout layout;
	
	private JToolBar toolBar;
	
	private JTabbedPane leftPanel;
	
	private TemplatePane detailsPanel;
	
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
		
		this.layout = WorkbenchLayout.LEFT;
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
		
		Properties props = new Properties();
		props.put(VelocityEngine.VM_LIBRARY, "/vm/details/common.vm");
		detailsPanel = new TemplatePane(props);
		
		leftPanel = new JTabbedPane();
		//leftPanel.setTabPlacement(JTabbedPane.LEFT);
		leftPanel.add(detailsPanel, "Details");
		
		workspace = new WorkspacePanel();
		
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
			splitPane.add(workspace);
		}
		else {
			splitPane.add(workspace);
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
	
	private void createWelcomeView() {
		AbstractEditor view = new WelcomeEditor();
		workspace.addView(view);
	}
	
	private void createDemoView() {
		AbstractEditor demoView = 
			new DemoEditor(40, 12);
		
		workspace.addView(demoView);
	}

	public void start() {
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	public TemplatePane getDetailsPane() {
		return detailsPanel;
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


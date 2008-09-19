package es.imim.bg.ztools.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import es.imim.bg.ztools.ui.views.View;

public class AppView extends JFrame {

	private static final long serialVersionUID = -6899584212813749990L;

	public JMenu menuFile;
	public JMenuItem menuiFileLoad;
	
	private JTabbedPane workspace;
	
	private StatusBar statusBar;
	
	public AppView() {
		createComponents();
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		pack();
	}

	private void createComponents() {
		setJMenuBar(createMenu());
		
		workspace = new JTabbedPane();
		add(workspace, BorderLayout.CENTER);
		
		statusBar = new StatusBar();
		
		add(statusBar, BorderLayout.SOUTH);
	}
	
	private JMenuBar createMenu() {
		
		menuiFileLoad = new JMenuItem("Load...");
		menuFile = new JMenu("File");
		menuFile.add(menuiFileLoad);
		
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		
		return menuBar;
	}

	public void addWorkspaceView(View view) {
		final String name = view.getName() != null ? view.getName() : "";
		final Icon icon = view.getIcon();
		final JPanel panel = view.getPanel();
		
		if (icon == null)
			workspace.addTab(name, panel);
		else
			workspace.addTab(name, icon, panel);
		
		workspace.setSelectedComponent(panel);
	}
	
	public void setStatusText(String text) {
		statusBar.setText(text);
		repaint();
	}
}

package org.gitools.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = -8072022883069269170L;

	private class StatusBarProgressMonitor extends DefaultProgressMonitor {
		
		public StatusBarProgressMonitor() {
			super();
		}
		
		public StatusBarProgressMonitor(IProgressMonitor parent) {
			super(parent);
		}
		
		@Override
		public void begin(String title, int totalWork) {
			super.begin(title, totalWork);
			
			setText(title);
		}
		
		@Override
		public void end() {
			super.end();
			
			setText("Ok");
		}
		
		@Override
		public IProgressMonitor subtask() {
			return new StatusBarProgressMonitor(this);
		}
	}
	
	private JLabel statusLabel;
	
	public StatusBar() {
		createComponents();
	}
	
	private void createComponents() {
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		setLayout(new BorderLayout());
		add(statusLabel, BorderLayout.CENTER);
	}
	
	public void setText(final String text) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					statusLabel.setText(text);
					statusLabel.repaint();
				}
			});
		} catch (Exception e) {
			e.printStackTrace(); //FIXME
		}
	}

	public IProgressMonitor createMonitor() {
		return new StatusBarProgressMonitor();
	}
}

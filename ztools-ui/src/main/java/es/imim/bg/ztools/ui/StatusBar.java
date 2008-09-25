package es.imim.bg.ztools.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import es.imim.bg.progressmonitor.DefaultProgressMonitor;
import es.imim.bg.progressmonitor.ProgressMonitor;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = -8072022883069269170L;

	private class StatusBarProgressMonitor extends DefaultProgressMonitor {
		
		public StatusBarProgressMonitor() {
			super();
		}
		
		public StatusBarProgressMonitor(ProgressMonitor parent) {
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
		public ProgressMonitor subtask() {
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
	
	public void setText(String text) {
		statusLabel.setText(text);
		statusLabel.repaint();
	}

	public ProgressMonitor createMonitor() {
		return new StatusBarProgressMonitor();
	}
}

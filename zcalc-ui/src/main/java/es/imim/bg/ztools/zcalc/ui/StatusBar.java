package es.imim.bg.ztools.zcalc.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = -8072022883069269170L;

	private JLabel statusLabel;
	
	public StatusBar() {
		createComponents();
	}
	
	private void createComponents() {
		statusLabel = new JLabel();
		//statusLabel.setHorizontalAlignment();
		add(statusLabel, BorderLayout.CENTER);
	}
	
	public void setText(String text) {
		statusLabel.setText(text);
	}
}

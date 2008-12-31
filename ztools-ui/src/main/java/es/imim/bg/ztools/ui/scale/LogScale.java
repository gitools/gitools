package es.imim.bg.ztools.ui.scale;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import es.imim.bg.colorscale.PValueLogColorScale;

public class LogScale extends AbstractScale {

	public LogScale() {
		super(
			new PValueLogColorScale(),
			createConfigPanel());
	}

	private static JPanel createConfigPanel() {
		JPanel panel = new JPanel();
		
		
		panel.setLayout(new FlowLayout());
		
		return panel;
	}
}

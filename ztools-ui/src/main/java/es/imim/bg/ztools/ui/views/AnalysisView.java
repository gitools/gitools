package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.ui.panels.analysis.AnalysisPanel;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = -3362979522018421333L;
	
	private AnalysisPanel panel;
	
	public AnalysisView(Analysis analysis) {
		this.panel = new AnalysisPanel(analysis);
		
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}
}

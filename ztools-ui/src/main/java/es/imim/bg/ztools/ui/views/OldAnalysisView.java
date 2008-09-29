package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;

public class OldAnalysisView extends AbstractView {

	private static final long serialVersionUID = -3362979522018421333L;
	
	private AnalysisView panel;
	
	public OldAnalysisView(Analysis analysis) {
		this.panel = new AnalysisView(
				new AnalysisModel(analysis));
		
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	@Override
	public Object getModel() {
		return panel.getAnalysisModel();
	}

	public Object getResultsPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}

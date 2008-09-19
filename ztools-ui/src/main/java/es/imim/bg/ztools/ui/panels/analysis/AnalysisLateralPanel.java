package es.imim.bg.ztools.ui.panels.analysis;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.ui.panels.results.ResultsCellParamsPanel;

public class AnalysisLateralPanel extends JPanel {

	private static final long serialVersionUID = -3844597759805854811L;

	private Analysis analysis;

	private ResultsCellParamsPanel cellParamsPanel;
	
	public AnalysisLateralPanel(Analysis analysis) {
		
		this.analysis = analysis;
		
		createComponents();
	}
	
	private void createComponents() {
		
		cellParamsPanel = new ResultsCellParamsPanel(
				analysis.getResults().getParamNames());
		
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		setLayout(new BorderLayout());
		add(cellParamsPanel);
	}
	
	public ResultsCellParamsPanel getCellParamsPanel() {
		return cellParamsPanel;
	}
}

package es.imim.bg.ztools.ui.views.analysis;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.views.results.ResultsCellParamsPanel;

public class AnalysisLateralPanel extends JPanel {

	private static final long serialVersionUID = -3844597759805854811L;

	private AnalysisModel analysisModel;

	private ResultsCellParamsPanel cellParamsPanel;
	
	public AnalysisLateralPanel(AnalysisModel analysisModel) {
		
		this.analysisModel = analysisModel;
		
		createComponents();
	}
	
	private void createComponents() {
		
		ResultsModel rmodel = analysisModel.getResultsModel();
		
		String[] names = new String[rmodel.getParamCount()];
		for (int i = 0; i < names.length; i++)
			names[i] = rmodel.getParamName(i);
		
		cellParamsPanel = new ResultsCellParamsPanel(names);
		
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		setLayout(new BorderLayout());
		add(cellParamsPanel);
	}
	
	public ResultsCellParamsPanel getCellParamsPanel() {
		return cellParamsPanel;
	}
}

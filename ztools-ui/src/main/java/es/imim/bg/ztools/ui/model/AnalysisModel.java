package es.imim.bg.ztools.ui.model;

import es.imim.bg.ztools.model.Analysis;

public class AnalysisModel {

	private Analysis analysis;
	
	private ResultsModel resultsModel;
	
	public AnalysisModel(Analysis analysis) {
		this.analysis = analysis;
		
		this.resultsModel = new ResultsModel(analysis.getResults());
	}
	
	public Analysis getAnalysis() {
		return analysis;
	}
	
	public ResultsModel getResultsModel() {
		return resultsModel;
	}
}

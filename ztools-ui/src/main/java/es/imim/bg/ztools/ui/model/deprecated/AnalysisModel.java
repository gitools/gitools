package es.imim.bg.ztools.ui.model.deprecated;

import es.imim.bg.ztools.model.AbstractModel;
import es.imim.bg.ztools.model.Analysis;

@Deprecated
public class AnalysisModel extends AbstractModel {

	public static final String SELECTION_MODE_PROPERTY = "selectionMode";
	
	private Analysis analysis;
	
	private ResultsModel resultsModel;

	protected SelectionMode selectionMode;
	
	public AnalysisModel(Analysis analysis) {
		this.analysis = analysis;
		
		this.resultsModel = new ResultsModel(analysis.getResults());
		
		this.selectionMode = SelectionMode.columns;
	}
	
	public Analysis getAnalysis() {
		return analysis;
	}
	
	public ResultsModel getResultsModel() {
		return resultsModel;
	}

	/*public void setSelectionMode(SelectionMode mode) {
		SelectionMode old = this.selectionMode;
		this.selectionMode = mode;
		resultsModel.setSelectionMode(mode);
		firePropertyChange(SELECTION_MODE_PROPERTY, old, mode);
	}*/

	public SelectionMode getSelectionMode() {
		return selectionMode;
	}
}

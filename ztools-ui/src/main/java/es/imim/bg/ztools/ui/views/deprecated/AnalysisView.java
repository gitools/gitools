package es.imim.bg.ztools.ui.views.deprecated;

import es.imim.bg.ztools.ui.model.deprecated.AnalysisModel;
import es.imim.bg.ztools.ui.model.deprecated.ISectionModel;

public class AnalysisView extends SectionsView {

	private static final long serialVersionUID = 9073825159199447872L;
	
	//private Analysis analysis;
	
	private AnalysisModel analysisModel;
	
	public AnalysisView(final AnalysisModel analysisModel) {
		ISectionModel[] sectionModels = new ISectionModel[] {
			analysisModel.getResultsModel()
		};
		
		setSectionModels(sectionModels, 0);
		
		this.analysisModel = analysisModel;
		
		createComponents();
	}
	
	public AnalysisModel getAnalysisModel() {
		return analysisModel;
	}
}

package es.imim.bg.ztools.ui.views;

import es.imim.bg.ztools.ui.model.ISectionModel;
import es.imim.bg.ztools.ui.model.ResultsModel;

public class DemoView extends SectionsView {

	private static final long serialVersionUID = 2467164492764056062L;

	protected ResultsModel resultsModel;
	
	public DemoView(ResultsModel resultsModel) {
		this.resultsModel = resultsModel;
		
		ISectionModel[] sectionModels = new ISectionModel[] {
				resultsModel
			};
			
		setSectionModels(sectionModels, 0);
		
		createComponents();
	}
}

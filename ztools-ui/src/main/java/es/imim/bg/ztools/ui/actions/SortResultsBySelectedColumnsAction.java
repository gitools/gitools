package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.AnalysisView;

public class SortResultsBySelectedColumnsAction extends BaseAction {

	private static final long serialVersionUID = -582380114189586206L;

	public SortResultsBySelectedColumnsAction(AppFrame app) {
		super(app, "Sort using selected columns");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = getCurrentView();
		if (!(view instanceof AnalysisView))
			return;
		
		AnalysisModel amodel = (AnalysisModel) view.getModel();
		ResultsModel rmodel = amodel.getResultsModel();
		
		//SortResults-BySelectedColumns-Command cmd = ...
	}

}

package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;
import es.imim.bg.ztools.ui.views.results.ResultsView;

public class HideSelectedColumnsAction extends BaseAction {

	private static final long serialVersionUID = 1453040322414160605L;

	public HideSelectedColumnsAction() {
		super("Hide selected columns");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
AbstractView view = getSelectedView();
		
		ColorMatrixPanel colorMatrixPanel = null;
		ResultsModel resultsModel = null;
		int[] indices = null;
		
		if (view instanceof AnalysisView) {
			AnalysisView aview = (AnalysisView) view; 
			resultsModel = aview
				.getAnalysisModel()
				.getResultsModel();
			
			colorMatrixPanel = aview
				.getResultsPanel()
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();
		}
		else if (view instanceof ResultsView) {
			resultsModel = ((ResultsView) view)
				.getResultsModel();
			
			colorMatrixPanel = ((ResultsView) view)
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();
		}
		
		if (resultsModel == null)
			return;
		
		resultsModel.removeColumns(indices);
		
		colorMatrixPanel.clearSelection();
		colorMatrixPanel.refresh();
	}

}

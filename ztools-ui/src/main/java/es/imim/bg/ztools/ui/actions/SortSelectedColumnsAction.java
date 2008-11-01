package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;
import es.imim.bg.ztools.ui.views.results.ResultsView;

public class SortSelectedColumnsAction extends BaseAction {

	private static final long serialVersionUID = -582380114189586206L;

	public SortSelectedColumnsAction() {
		super("Sort selected columns");
		
		setDesc("Sort selected columns");
		setSmallIconFromResource(IconNames.sortSelectedColumns16);
		setLargeIconFromResource(IconNames.sortSelectedColumns24);
		setMnemonic(KeyEvent.VK_S);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = getSelectedView();
		
		ColorMatrixPanel colorMatrixPanel = null;
		ResultsModel resultsModel = null;
		int[] indices = null;
		int selParamIndex = 0;
		
		if (view instanceof AnalysisView) {
			AnalysisView aview = (AnalysisView) view; 
			resultsModel = aview
				.getAnalysisModel()
				.getResultsModel();
			
			selParamIndex = aview
				.getResultsPanel()
				.getSelectedParamIndex();
			
			colorMatrixPanel = aview
				.getResultsPanel()
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();
		}
		else if (view instanceof ResultsView) {
			resultsModel = ((ResultsView) view)
				.getResultsModel();
			
			selParamIndex = ((ResultsView) view)
				.getSelectedParamIndex();
				
			colorMatrixPanel = ((ResultsView) view)
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();
		}
		
		if (resultsModel == null)
			return;
		
		/*List<SortCriteria> criteriaList = 
			new ArrayList<SortCriteria>(indices.length);
		
		for (int i = 0; i <  indices.length; i++)
			criteriaList.add(new SortCriteria(
					indices[i], selParamIndex, true));*/
		
		resultsModel.sortByFunc(indices, selParamIndex);
		
		view.refresh();
	}

}

package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.TableView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;

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
		
		//FIXME:
		if (true)
			return;
		
		//ColorMatrixPanel colorMatrixPanel = null;
		ITableModel tableModel = null;
		int[] indices = null;
		int selParamIndex = 0;
		
		if (view instanceof AnalysisView) {
			AnalysisView aview = (AnalysisView) view;
			AnalysisModel analysisModel = aview
				.getAnalysisModel();
			tableModel = analysisModel
				.getResultsModel();
			
			/*selParamIndex = aview
				.getResultsPanel()
				.getSelectedParamIndex();*/
			
			//TODO: selParamIndex = analysisModel.getSelectedParamIndex();
			
			/*colorMatrixPanel = aview
				.getResultsPanel()
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();*/
		}
		else if (view instanceof TableView) {
			tableModel = ((TableView) view)
				.getTableModel();
			
			/*selParamIndex = ((TableView) view)
				.getSelectedParamIndex();*/
			
			selParamIndex = 0;
				
			/*colorMatrixPanel = ((TableView) view)
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();*/
		}
		
		if (tableModel == null)
			return;
		
		/*List<SortCriteria> criteriaList = 
			new ArrayList<SortCriteria>(indices.length);
		
		for (int i = 0; i <  indices.length; i++)
			criteriaList.add(new SortCriteria(
					indices[i], selParamIndex, true));*/
		
		//TODO: sort func must be independent of param index
		tableModel.sortByFunc(indices, selParamIndex);
		
		view.refresh();
	}

}

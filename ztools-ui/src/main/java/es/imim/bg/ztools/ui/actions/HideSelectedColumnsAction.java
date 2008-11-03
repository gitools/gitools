package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.TableView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;

public class HideSelectedColumnsAction extends BaseAction {

	private static final long serialVersionUID = 1453040322414160605L;

	public HideSelectedColumnsAction() {
		super("Hide selected columns");
		
		setDesc("Hide selected columns");
		setSmallIconFromResource(IconNames.columnHide16);
		setLargeIconFromResource(IconNames.columnHide24);
		setMnemonic(KeyEvent.VK_O);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = getSelectedView();
		
		//ColorMatrixPanel colorMatrixPanel = null;
		ITableModel resultsModel = null;
		int[] indices = null;
		
		if (view instanceof AnalysisView) {
			AnalysisView aview = (AnalysisView) view; 
			resultsModel = aview
				.getAnalysisModel()
				.getResultsModel();
			
			/*colorMatrixPanel = aview
				.getResultsPanel()
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();*/
		}
		else if (view instanceof TableView) {
			resultsModel = ((TableView) view)
				.getTableModel();
			
			/*colorMatrixPanel = ((TableView) view)
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedColumns();*/
		}
		
		if (resultsModel == null)
			return;
		
		indices = resultsModel.getSelectedColumns();
		
		resultsModel.removeColumns(indices);
		
		/*colorMatrixPanel.clearSelection();
		colorMatrixPanel.refresh();*/
	}

}

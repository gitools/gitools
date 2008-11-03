package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.TableView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;

public class HideSelectedRowsAction extends BaseAction {

	private static final long serialVersionUID = 1453040322414160605L;

	public HideSelectedRowsAction() {
		super("Hide selected rows");
		
		setDesc("Hide selected rows");
		setSmallIconFromResource(IconNames.rowHide16);
		setLargeIconFromResource(IconNames.rowHide24);
		setMnemonic(KeyEvent.VK_W);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = getSelectedView();
		
		ColorMatrixPanel colorMatrixPanel = null;
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
				.getSelectedRows();*/
		}
		else if (view instanceof TableView) {
			resultsModel = ((TableView) view)
				.getTableModel();
			
			/*colorMatrixPanel = ((TableView) view)
				.getColorMatrixPanel();
			
			indices = colorMatrixPanel
				.getSelectedRows();*/
		}
		
		if (resultsModel == null)
			return;
		
		indices = resultsModel.getSelectedRows();;
		
		resultsModel.removeRows(indices);
		
		/*colorMatrixPanel.clearSelection();
		colorMatrixPanel.refresh();*/
	}

}
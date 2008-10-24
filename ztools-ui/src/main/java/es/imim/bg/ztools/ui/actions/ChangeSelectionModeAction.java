package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.model.SelectionMode;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;
import es.imim.bg.ztools.ui.views.results.ResultsView;

public class ChangeSelectionModeAction extends BaseAction {

	private static final long serialVersionUID = -70164005532296778L;

	protected SelectionMode mode;
	
	public ChangeSelectionModeAction(SelectionMode mode) {
		super(null);
		switch (mode) {
		case columns:
			setName("Column selection mode");
			setSmallIconFromResource(IconNames.columnSelection16);
			setLargeIconFromResource(IconNames.columnSelection24);
			setMnemonic(KeyEvent.VK_C);
			break;
		case rows:
			setName("Row selection mode");
			setSmallIconFromResource(IconNames.rowSelection16);
			setLargeIconFromResource(IconNames.rowSelection24);
			setMnemonic(KeyEvent.VK_R);
			break;
		case cells:
			setName("Cell selection mode");
			setSmallIconFromResource(IconNames.cellSelection16);
			setLargeIconFromResource(IconNames.cellSelection24);
			setMnemonic(KeyEvent.VK_E);
			break;
		}
		this.mode = mode;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = getSelectedView();
		
		if (view instanceof AnalysisView) {
			AnalysisView aview = (AnalysisView) view; 
			AnalysisModel analysisModel = aview
				.getAnalysisModel();
			analysisModel.setSelectionMode(mode);
		}
		else if (view instanceof ResultsView) {
			ResultsModel resultsModel = ((ResultsView) view)
				.getResultsModel();
			if (resultsModel != null)
				resultsModel.setSelectionMode(mode);
		}
		
		AppFrame.instance()
			.setStatusText("Selection mode changed to " + mode.toString() + ".");
	}

}

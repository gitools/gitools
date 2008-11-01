package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;
import es.imim.bg.ztools.ui.views.results.ResultsView;

public class UnselectAllAction extends BaseAction {

	private static final long serialVersionUID = 1581417292789818975L;

	public UnselectAllAction() {
		super("Unselect all");
		
		setDesc("Unselect all");
		setSmallIconFromResource(IconNames.unselectAll16);
		setLargeIconFromResource(IconNames.unselectAll24);
		setMnemonic(KeyEvent.VK_U);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = getSelectedView();
		
		ColorMatrixPanel colorMatrixPanel = null;
		if (view instanceof AnalysisView) {
			colorMatrixPanel = ((AnalysisView) view)
				.getResultsPanel()
				.getColorMatrixPanel();
		}
		else if (view instanceof ResultsView) {
			colorMatrixPanel = ((ResultsView) view)
				.getColorMatrixPanel();
		}
		
		if (colorMatrixPanel == null)
			return;
		
		colorMatrixPanel.clearSelection();
		
		colorMatrixPanel.refresh();
	}

}

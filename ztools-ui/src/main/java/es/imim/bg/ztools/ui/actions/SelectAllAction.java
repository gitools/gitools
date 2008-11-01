package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;
import es.imim.bg.ztools.ui.views.results.ResultsView;

public class SelectAllAction extends BaseAction {

	private static final long serialVersionUID = 3088237733885396229L;

	public SelectAllAction() {
		super("Select all");
		
		setDesc("Select all");
		setSmallIconFromResource(IconNames.selectAll16);
		setLargeIconFromResource(IconNames.selectAll24);
		setMnemonic(KeyEvent.VK_A);
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
		
		colorMatrixPanel.selectAll();
		
		colorMatrixPanel.refresh();
	}

}

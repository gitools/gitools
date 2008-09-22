package es.imim.bg.ztools.ui.panels.analysis;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel.ColorMatrixListener;
import es.imim.bg.ztools.ui.panels.results.ResultsPanel;

public class AnalysisPanel extends JPanel {

	private static final long serialVersionUID = 9073825159199447872L;

	private AnalysisLateralPanel lateralPanel;
	private ResultsPanel resultsPanel;
	
	private Analysis analysis;
	
	public AnalysisPanel(Analysis analysis) {
		
		this.analysis = analysis;
		
		createComponents();
	}
	
	private void createComponents() {
		
		final Results results = analysis.getResults();
		
		lateralPanel = new AnalysisLateralPanel(analysis);
		resultsPanel = new ResultsPanel(results);
		
		final String[] values = new String[results.getParamNames().length];
		
		resultsPanel.getColorMatrixPanel().addListener(new ColorMatrixListener() {
			@Override
			public void selectionChanged() {
			
				int colIndex = resultsPanel.getColorMatrixPanel().getSelectedLeadColumn();
				int rowIndex = resultsPanel.getColorMatrixPanel().getSelectedLeadRow();
				
				if (colIndex < 0 || rowIndex < 0)
					return;
				
				int numParams = results.getParamNames().length;
				
				for (int i = 0; i < numParams; i++)
					values[i] = Double.toString(
							results.getDataValue(colIndex, rowIndex, i));
				
				lateralPanel.getCellParamsPanel().setValues(values);
			}
		});
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.add(lateralPanel);
		splitPane.add(resultsPanel);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}
}

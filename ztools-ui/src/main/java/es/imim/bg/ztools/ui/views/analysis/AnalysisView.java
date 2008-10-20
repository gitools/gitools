package es.imim.bg.ztools.ui.views.analysis;

import java.awt.BorderLayout;

import javax.swing.JSplitPane;

import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel.ColorMatrixListener;
import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.results.ResultsView;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = 9073825159199447872L;

	private AnalysisInfoRender infoRender;
	private AnalysisLateralPanel lateralPanel;
	private ResultsView resultsView;
	
	//private Analysis analysis;
	
	private AnalysisModel analysisModel;
	
	public AnalysisView(AnalysisModel analysisModel) {
		
		this.analysisModel = analysisModel;
		
		this.infoRender = new AnalysisInfoRender(analysisModel);
		
		createComponents();
	}
	
	private void createComponents() {
		
		//final Results results = analysis.getResults();
		
		lateralPanel = new AnalysisLateralPanel(analysisModel);
		resultsView = new ResultsView(analysisModel.getResultsModel());
		
		final ResultsModel resultsModel = analysisModel.getResultsModel();
		final String[] values = new String[resultsModel.getParamCount()];
		
		resultsView.getColorMatrixPanel().addListener(new ColorMatrixListener() {
			@Override
			public void selectionChanged() {
			
				int colIndex = resultsView.getColorMatrixPanel().getSelectedLeadColumn();
				int rowIndex = resultsView.getColorMatrixPanel().getSelectedLeadRow();
				
				if (colIndex < 0 || rowIndex < 0) {
					infoRender.setValues(null);
					return;
				}
				
				int numParams = resultsModel.getParamCount();
				
				for (int i = 0; i < numParams; i++)
					values[i] = Double.toString(
							resultsModel.getValue(colIndex, rowIndex, i));
				
				infoRender.setColName(resultsModel.getColumnName(colIndex));
				infoRender.setRowName(resultsModel.getRowName(rowIndex));
				infoRender.setValues(values);
				lateralPanel.showInfo(infoRender.toString());
			}
		});
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.add(lateralPanel);
		splitPane.add(resultsView);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	public AnalysisModel getAnalysisModel() {
		return analysisModel;
	}

	public ResultsView getResultsPanel() {
		return resultsView;
	}
	
	@Override
	public Object getModel() {
		return analysisModel;
	}

	@Override
	public void refresh() {
		resultsView.refresh();
	}
	
	@Override
	public void enableActions() {
		resultsView.enableActions();
	}
	
	@Override
	public void disableActions() {
		resultsView.disableActions();
	}
}

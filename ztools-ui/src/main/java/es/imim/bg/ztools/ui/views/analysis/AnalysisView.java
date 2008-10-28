package es.imim.bg.ztools.ui.views.analysis;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSplitPane;

import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.results.ResultsView;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = 9073825159199447872L;

	private AnalysisLateralPanel lateralPanel;
	private ResultsView resultsView;
	
	//private Analysis analysis;
	
	private AnalysisModel analysisModel;
	
	public AnalysisView(final AnalysisModel analysisModel) {
		
		this.analysisModel = analysisModel;
		
		createComponents();
		
		analysisModel.getResultsModel().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						lateralPanel.showInfo(
								analysisModel.getResultsModel().getHtmlInfo());
					}			
		});
	}
	
	private void createComponents() {
		
		//final Results results = analysis.getResults();
		
		lateralPanel = new AnalysisLateralPanel(analysisModel);
		resultsView = new ResultsView(analysisModel.getResultsModel());
		
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

package es.imim.bg.ztools.ui.views.analysis;

import java.awt.BorderLayout;

import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.TableAndInfoView;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = 9073825159199447872L;

	private TableAndInfoView tableView;
	
	//private Analysis analysis;
	
	private AnalysisModel analysisModel;
	
	public AnalysisView(final AnalysisModel analysisModel) {
		
		this.analysisModel = analysisModel;
		
		createComponents();
	}
	
	private void createComponents() {
		
		//final Results results = analysis.getResults();
		
		tableView = new TableAndInfoView(
				analysisModel.getResultsModel());
		
		setLayout(new BorderLayout());
		add(tableView, BorderLayout.CENTER);
	}

	public AnalysisModel getAnalysisModel() {
		return analysisModel;
	}

	public TableAndInfoView getResultsPanel() {
		return tableView;
	}
	
	@Override
	public Object getModel() {
		return analysisModel;
	}

	@Override
	public void refresh() {
		tableView.refresh();
	}
	
	@Override
	public void enableActions() {
		tableView.enableActions();
	}
	
	@Override
	public void disableActions() {
		tableView.disableActions();
	}
}

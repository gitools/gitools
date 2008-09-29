package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.views.results.ResultsView;


public class OldResultsView extends AbstractView {

	private static final long serialVersionUID = 8665102057021968675L;

	private ResultsView panel;
	
	public OldResultsView(Results results) {
		ResultsModel model = new ResultsModel(results);
		this.panel = new ResultsView(model);
		
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	@Override
	public Object getModel() {
		return panel.getResultsModel();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}

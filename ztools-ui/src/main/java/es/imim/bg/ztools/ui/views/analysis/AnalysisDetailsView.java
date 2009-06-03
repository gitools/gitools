package es.imim.bg.ztools.ui.views.analysis;

import java.awt.BorderLayout;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.ui.panels.TemplatePane;
import es.imim.bg.ztools.ui.views.AbstractView;

public class AnalysisDetailsView extends AbstractView {

	private static final long serialVersionUID = 8258025724628410016L;

	private Analysis analysis;
	
	private TemplatePane templatePane;

	public AnalysisDetailsView(Analysis analysis) {
		this.analysis = analysis;
		
		createComponents();
	}
	
	private void createComponents() {
		templatePane = new TemplatePane();
		
		setLayout(new BorderLayout());
		add(templatePane);
	}

	@Override
	public Object getModel() {
		return analysis;
	}

	@Override
	public void refresh() {
	}
}

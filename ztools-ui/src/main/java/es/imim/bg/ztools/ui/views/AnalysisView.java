package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;

import javax.swing.JSplitPane;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = -3362979522018421333L;
	
	private Analysis analysis;
	
	public AnalysisView(Analysis analysis) {
		
		this.analysis = analysis;
		
		createComponents();
	}
	
	private void createComponents() {
		
		Results results = analysis.getResults();
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		//splitPane.add(leftPanel);
		//splitPane.add(colorMatrix);
		splitPane.setDividerLocation(160);
		splitPane.setOneTouchExpandable(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}
}

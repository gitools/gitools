package es.imim.bg.ztools.zcalc.ui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSplitPane;

import es.imim.bg.ztools.resources.ResultsFile;
import es.imim.bg.ztools.zcalc.ui.panels.MatrixPanel;
import es.imim.bg.ztools.zcalc.ui.panels.ParamAndScalePanel;
import es.imim.bg.ztools.zcalc.ui.views.matrix.ResultsMatrixTableModel;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = -3362979522018421333L;

	private ResultsFile resultsFile;
	
	private ResultsMatrixTableModel model;
	
	private ParamAndScalePanel leftPanel;
	private MatrixPanel matrixPanel;
	
	public AnalysisView(ResultsFile resultsFile) {
		
		this.resultsFile = resultsFile;
		
		model = new ResultsMatrixTableModel(
						resultsFile.getGroupNames(), 
						resultsFile.getCondNames(), 
						resultsFile.getResults(),
						0);
		
		createComponents();
	}
	
	private void createComponents() {
		leftPanel = new ParamAndScalePanel(
				resultsFile.getParamNames(),
				null);
		leftPanel.setPreferredSize(new Dimension(100, 100));
		
		matrixPanel = new MatrixPanel();
		matrixPanel.setModel(model);
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.add(leftPanel);
		splitPane.add(matrixPanel);
		splitPane.setDividerLocation(160);
		splitPane.setOneTouchExpandable(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
		//add(matrixPanel, BorderLayout.CENTER);
	}
}

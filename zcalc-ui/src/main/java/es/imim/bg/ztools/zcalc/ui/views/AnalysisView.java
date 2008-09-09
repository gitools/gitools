package es.imim.bg.ztools.zcalc.ui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSplitPane;

import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.ztools.resources.ResultsFile;
import es.imim.bg.ztools.zcalc.ui.panels.MatrixPanel;
import es.imim.bg.ztools.zcalc.ui.panels.ParamAndScalePanel;
import es.imim.bg.ztools.zcalc.ui.views.matrix.ResultsMatrixTableModel;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = -3362979522018421333L;

	//private ResultsFile resultsFile;
	
	private String[] rowNames;
	private String[] colNames;
	private String[] paramNames;
	private ObjectMatrix2D data;
	
	private ResultsMatrixTableModel model;
	
	private ParamAndScalePanel leftPanel;
	private MatrixPanel matrixPanel;
	
	public AnalysisView(
			String[] rowNames,
			String[] colNames,
			String[] paramNames,
			ObjectMatrix2D data) {
		
		//this.resultsFile = resultsFile;
		
		this.rowNames = rowNames;
		this.colNames = colNames;
		this.paramNames = paramNames;
		this.data = data;
		
		model = new ResultsMatrixTableModel(
						rowNames, 
						colNames, 
						data,
						0);
		
		createComponents();
	}
	
	private void createComponents() {
		leftPanel = new ParamAndScalePanel(
				paramNames,
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
	}
}

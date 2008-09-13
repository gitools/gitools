package es.imim.bg.ztools.zcalc.ui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSplitPane;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrix;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixModel;
import es.imim.bg.ztools.zcalc.ui.old.panels.ParamAndScalePanel;

public class AnalysisView extends AbstractView {

	private static final long serialVersionUID = -3362979522018421333L;
	
	private ParamAndScalePanel leftPanel;
	private ColorMatrix colorMatrix;
	
	private Analysis analysis;
	
	public AnalysisView(Analysis analysis) {
		
		this.analysis = analysis;
		
		createComponents();
	}
	
	private void createComponents() {
		
		Results results = analysis.getResults();
		
		final String[] paramNames = results.getParamNames();
		
		leftPanel = new ParamAndScalePanel(
				paramNames,
				null);
		
		leftPanel.setPreferredSize(new Dimension(100, 100));
		
		leftPanel.addParameterChangedListener(new ParamAndScalePanel.ParamChangeListener() {
			@Override
			public void paramChange(String name) {
				int i = 0;
				boolean done = false;
				while (!done && i < paramNames.length) {
					if (paramNames[i].equals(name)) {
						//model.setParamIndex(i);
						done = true;
					}
					i++;
				}
				//matrixPanel.refresh();
			}
		});
		
		colorMatrix = new ColorMatrix();
		colorMatrix.setModel(new ColorMatrixModel() {
			@Override
			public int getColumnCount() {
				return analysis.getResults().getData().columns();
			}
			@Override
			public String getColumnName(int column) {
				return analysis.getResults().getColNames()[column];
			}
			@Override
			public int getRowCount() {
				return analysis.getResults().getData().rows();
			}
			@Override
			public String getRowName(int row) {
				return analysis.getResults().getRowNames()[row];
			}
			@Override
			public Double getValue(int row, int column) {
				return analysis.getResults().getDataValue(column, row, 2); //FIXME paramIndex
			}
		});
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.add(leftPanel);
		splitPane.add(colorMatrix);
		splitPane.setDividerLocation(160);
		splitPane.setOneTouchExpandable(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}
}

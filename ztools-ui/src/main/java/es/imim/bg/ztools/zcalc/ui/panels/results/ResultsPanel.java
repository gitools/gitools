package es.imim.bg.ztools.zcalc.ui.panels.results;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrix;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixModel;

public class ResultsPanel extends JPanel {

	private static final long serialVersionUID = -540561086703759209L;

	private Results results;
	
	private int paramIndex;
	
	private ColorMatrix colorMatrix;
	
	public ResultsPanel(Results results) {
		
		this.results = results;
		
		paramIndex = results.getParamIndex("right-p-value");
		
		createComponents();
	}

	private void createComponents() {
		
		colorMatrix = new ColorMatrix();
		colorMatrix.setModel(new ColorMatrixModel() {
			@Override
			public int getColumnCount() {
				return results.getData().columns();
			}
			@Override
			public String getColumnName(int column) {
				return results.getColNames()[column];
			}
			@Override
			public int getRowCount() {
				return results.getData().rows();
			}
			@Override
			public String getRowName(int row) {
				return results.getRowNames()[row];
			}
			@Override
			public Double getValue(int row, int column) {
				return results.getDataValue(column, row, paramIndex);
			}
		});

		setLayout(new BorderLayout());
		add(colorMatrix, BorderLayout.CENTER);
	}
	
	
}

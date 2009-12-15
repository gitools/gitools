package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

import org.gitools.stats.mtc.MultipleTestCorrection;
import org.gitools.matrix.MatrixUtils;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;

public class MtcAction extends BaseAction {

	private static final long serialVersionUID = 991170566166881702L;

	protected MultipleTestCorrection mtc;
	
	public MtcAction(MultipleTestCorrection mtc) {
		super(mtc.getName());
		
		setDesc("Calculate " + mtc.getName() + " multiple test correction");
		
		this.mtc = mtc;
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		final IMatrixView matrixView = getMatrixView();
		
		if (matrixView == null)
			return;

		IElementAdapter cellAdapter = matrixView.getCellAdapter();
		
		final int propIndex = matrixView.getSelectedPropertyIndex();
		final int corrPropIndex = 
			MatrixUtils.correctedValueIndex(
				cellAdapter, cellAdapter.getProperty(propIndex));
		
		if (corrPropIndex < 0) {
			JOptionPane.showMessageDialog(AppFrame.instance(),
				    "The property selected doesn't allow multiple test correction.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		IMatrix contents = matrixView.getContents();
		
		int rowCount = contents.getRowCount();
		int columnCount = contents.getColumnCount();
		
		DoubleMatrix2D values = DoubleFactory2D.dense.make(rowCount, columnCount);
		
		for (int col = 0; col < columnCount; col++)
			for (int row = 0; row < rowCount; row++)
				values.setQuick(row, col, 
						MatrixUtils.doubleValue(
								contents.getCellValue(row, col, propIndex)));
		
		for (int col = 0; col < columnCount; col++) {
			DoubleMatrix1D columnValues = values.viewColumn(col).viewSelection(new DoubleProcedure() {
				@Override
				public boolean apply(double v) {
					return !Double.isNaN(v);
				}
			});
			mtc.correct(columnValues);
		}
		
		for (int col = 0; col < columnCount; col++)
			for (int row = 0; row < rowCount; row++)
				contents.setCellValue(row, col, corrPropIndex, 
						values.getQuick(row, col));
		
		AppFrame.instance()
			.setStatusText(mtc.getName() + " done.");
	}
}

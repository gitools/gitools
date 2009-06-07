package org.gitools.ui.actions.table;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

import org.gitools.stats.mtc.MultipleTestCorrection;
import org.gitools.model.table.ITable;
import org.gitools.model.table.ITableContents;
import org.gitools.model.table.TableUtils;
import org.gitools.model.table.element.IElementAdapter;

public class MtcAction extends BaseAction {

	private static final long serialVersionUID = 991170566166881702L;

	protected MultipleTestCorrection mtc;
	
	public MtcAction(MultipleTestCorrection mtc) {
		super(mtc.getName());
		
		setDesc("Calculate " + mtc.getName() + " multiple test correction");
		
		this.mtc = mtc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		final ITable table = getTable();
		
		if (table == null)
			return;

		IElementAdapter cellAdapter = table.getCellAdapter();
		
		final int propIndex = table.getSelectedPropertyIndex();
		final int corrPropIndex = 
			TableUtils.correctedValueIndex(
				cellAdapter, cellAdapter.getProperty(propIndex));
		
		if (corrPropIndex < 0) {
			JOptionPane.showMessageDialog(AppFrame.instance(),
				    "The property selected doesn't allow multiple test correction.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ITableContents contents = table.getContents();
		
		int rowCount = contents.getRowCount();
		int columnCount = contents.getColumnCount();
		
		DoubleMatrix2D values = DoubleFactory2D.dense.make(rowCount, columnCount);
		
		for (int col = 0; col < columnCount; col++)
			for (int row = 0; row < rowCount; row++)
				values.setQuick(row, col, 
						TableUtils.doubleValue(
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

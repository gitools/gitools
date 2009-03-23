package es.imim.bg.ztools.ui.views;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.ResultsMatrix;
import es.imim.bg.ztools.table.ResultsMatrixTableContentsAdapter;
import es.imim.bg.ztools.table.Table;
import es.imim.bg.ztools.table.element.array.ArrayElementAdapter;
import es.imim.bg.ztools.table.element.basic.StringElementAdapter;
import es.imim.bg.ztools.ui.views.table.TableView;

public class DemoView extends TableView {

	private static final long serialVersionUID = 2467164492764056062L;
	
	public DemoView(int rows, int cols) {
		super(createTable(rows, cols));
		
		setName("Demo");
	}
	
	private static Table createTable(int rows, int cols) {		
		int k = 0;
		DoubleMatrix1D values = DoubleFactory1D.dense.random(2 * rows * cols);
		
		ObjectMatrix2D data = ObjectFactory2D.dense.make(rows, cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				double[] v = new double[] {
					values.getQuick(k++),
					values.getQuick(k++)
				};
				data.setQuick(row, col, v);
			}
		}
		
		final ObjectMatrix1D rowNames = ObjectFactory1D.dense.make(data.rows());
		for (int i = 0; i < rowNames.size(); i++)
			rowNames.setQuick(i, "row " + (i + 1));
		
		final ObjectMatrix1D colNames = ObjectFactory1D.dense.make(data.columns());
		for (int i = 0; i < colNames.size(); i++)
			colNames.setQuick(i, "col " + (i + 1));
		
		ResultsMatrix resultsMatrix = new ResultsMatrix(
				rowNames,
				colNames,   
				data,
				new StringElementAdapter(), 
				new StringElementAdapter(),
				new ArrayElementAdapter(new String[] {"p-value", "corrected-p-value"}));
		
		Analysis analysis = new Analysis();
		analysis.setResults(resultsMatrix);
		
		return new Table(
					new ResultsMatrixTableContentsAdapter(
								resultsMatrix));
	}
}

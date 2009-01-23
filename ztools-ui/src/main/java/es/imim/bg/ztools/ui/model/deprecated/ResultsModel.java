package es.imim.bg.ztools.ui.model.deprecated;

import java.util.ArrayList;
import java.util.HashMap;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import es.imim.bg.ztools.model.ResultsMatrix;
import es.imim.bg.ztools.model.elements.ElementAdapter;

public class ResultsModel 
		extends CubeSectionModel {

	private ResultsMatrix resultsMatrix;
	
	public ResultsModel(ResultsMatrix resultsMatrix) {
		super();
		
		this.resultsMatrix = resultsMatrix;

		initialize();
	}
	
	public void initialize() {
		columnCount = resultsMatrix.getColumnCount();
		visibleCols = new int[columnCount];
		for (int i = 0; i < columnCount; i++)
			visibleCols[i] = i;
		
		columnNames = new String[columnCount];
		for (int i = 0; i < columnCount; i++)
			columnNames[i] = resultsMatrix.getColumn(i).toString();
		
		rowCount = resultsMatrix.getRowCount();
		visibleRows = new int[rowCount];
		for (int i = 0; i < rowCount; i++)
			visibleRows[i] = i;
		rowNames = new String[rowCount];
		for (int i = 0; i < rowCount; i++)
			rowNames[i] = resultsMatrix.getRow(i).toString();
		
		paramCount = resultsMatrix.getCellAdapter().getPropertyCount();
		data = new ArrayList<DoubleMatrix2D>(paramCount);
		for (int i = 0; i < paramCount; i++) {
			DoubleMatrix2D matrix = DoubleFactory2D.dense.make(rowCount, columnCount);
			for (int row = 0; row < rowCount; row++)
				for (int col = 0; col < columnCount; col++)
					matrix.setQuick(row, col,
							(Double) resultsMatrix.getCellValue(
									row, col, i));
			data.add(matrix);
		}
		
		ElementAdapter cellsFacade = resultsMatrix.getCellAdapter();
		visibleParams = new int[paramCount];
		paramIndexMap = new HashMap<String, Integer>();
		paramNames = new String[paramCount];
		for (int i = 0; i < paramCount; i++) {
			visibleParams[i] = i;
			paramNames[i] = cellsFacade.getProperty(i).getId();
			paramIndexMap.put(paramNames[i], i);
		}
		
		//FIXME
		if (paramIndexMap.containsKey("right-p-value"))
			currentParam = paramIndexMap.get("right-p-value");
		else
			currentParam = 0;
		
		resetSelection();
	}
	
	@Override
	public String getName() {
		return "Results";
	}
}

package es.imim.bg.ztools.ui.model;

import java.util.ArrayList;
import java.util.HashMap;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix3D;
import es.imim.bg.ztools.model.ResultsMatrix;

public class ResultsModel 
		extends CubeSectionModel {

	private ResultsMatrix resultsMatrix;
	
	public ResultsModel(ResultsMatrix resultsMatrix) {
		super();
		
		this.resultsMatrix = resultsMatrix;

		initialize();
	}
	
	public void initialize() {
		columnCount = resultsMatrix.getData().columns();
		visibleCols = new int[columnCount];
		for (int i = 0; i < columnCount; i++)
			visibleCols[i] = i;
		columnNames = resultsMatrix.getColNames();
		
		rowCount = resultsMatrix.getData().rows();
		visibleRows = new int[rowCount];
		for (int i = 0; i < rowCount; i++)
			visibleRows[i] = i;
		rowNames = resultsMatrix.getRowNames();
		
		DoubleMatrix3D cube = resultsMatrix.getData();
		data = new ArrayList<DoubleMatrix2D>(cube.slices());
		for (int i = 0; i < cube.slices(); i++)
			data.add(cube.viewSlice(i));
		
		paramCount = cube.slices();
		visibleParams = new int[paramCount];
		paramIndexMap = new HashMap<String, Integer>();
		paramNames = resultsMatrix.getParamNames();
		for (int i = 0; i < paramCount; i++) {
			visibleParams[i] = i;
			paramIndexMap.put(paramNames[i], i);
		}
		
		//FIXME
		currentParam = paramIndexMap.get("right-p-value");
		
		resetSelection();
	}
	
	@Override
	public String getName() {
		return "Results";
	}
}

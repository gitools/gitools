package es.imim.bg.ztools.ui.model;

import java.util.ArrayList;
import java.util.HashMap;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix3D;
import es.imim.bg.ztools.model.Results;

public class ResultsModel 
		extends CubeSectionModel {

	private Results results;
	
	public ResultsModel(Results results) {
		super();
		
		this.results = results;

		initialize();
	}
	
	public void initialize() {
		columnCount = results.getData().columns();
		visibleCols = new int[columnCount];
		for (int i = 0; i < columnCount; i++)
			visibleCols[i] = i;
		columnNames = results.getColNames();
		
		rowCount = results.getData().rows();
		visibleRows = new int[rowCount];
		for (int i = 0; i < rowCount; i++)
			visibleRows[i] = i;
		rowNames = results.getRowNames();
		
		DoubleMatrix3D cube = results.getData();
		data = new ArrayList<DoubleMatrix2D>(cube.slices());
		for (int i = 0; i < cube.slices(); i++)
			data.add(cube.viewSlice(i));
		
		paramCount = cube.slices();
		visibleParams = new int[paramCount];
		paramIndexMap = new HashMap<String, Integer>();
		paramNames = results.getParamNames();
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

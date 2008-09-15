package es.imim.bg.ztools.model;

import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix3D;

public class Results {

	protected String[] colNames;
	protected String[] rowNames;
	protected String[] paramNames;
	
	protected DoubleMatrix3D data;

	public Results() {
	}
	
	public Results(
			String[] colNames, 
			String[] rowNames, 
			String[] paramNames,
			DoubleMatrix3D data) {
		
		this.colNames = colNames;
		this.rowNames = rowNames;
		this.paramNames = paramNames;
		this.data = data;
	}

	public final String[] getColNames() {
		return colNames;
	}

	public final void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	public final String[] getRowNames() {
		return rowNames;
	}

	public final void setRowNames(String[] rowNames) {
		this.rowNames = rowNames;
	}

	public final String[] getParamNames() {
		return paramNames;
	}

	public final void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public final DoubleMatrix3D getData() {
		return data;
	}

	public final void setData(DoubleMatrix3D data) {
		this.data = data;
	}

	public void createData() {
		data = DoubleFactory3D.dense.make(
				paramNames.length,
				rowNames.length,
				colNames.length
				);
	}
	
	public double getDataValue(int colIndex, int rowIndex, int paramIndex) {
		return data.getQuick(paramIndex, rowIndex, colIndex);
	}

	public void setDataValue(int colIndex, int rowIndex, int paramIndex, double value) {
		data.setQuick(paramIndex, rowIndex, colIndex, value);
	}
	
	public int getParamIndex(String name) {
		int index = 0;
		while (index < paramNames.length 
				&& !paramNames[index].equals(name))
			index++;
		
		if (index >= paramNames.length)
			throw new RuntimeException("Parameter called '" + name + "' doesn't exists.");
		
		return index;
	}
}

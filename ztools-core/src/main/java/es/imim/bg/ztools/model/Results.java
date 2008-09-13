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
				colNames.length, 
				rowNames.length, 
				paramNames.length);
	}
	
	public void setDataValue(int colIndex, int rowIndex, int paramIndex, double value) {
		data.setQuick(colIndex, rowIndex, paramIndex, value);
	}

	public double getDataValue(int colIndex, int rowIndex, int paramIndex) {
		return data.getQuick(colIndex, rowIndex, paramIndex);
	}
}

package org.gitools.model.table;

import javax.xml.bind.annotation.XmlTransient;


import cern.colt.matrix.DoubleMatrix2D;

/*@XmlType(
		propOrder = {
				"name"///, 
				"colNames", 
				"rowNames", 
				"data"///})*/
				
public final class DoubleMatrix { 
	//implements IMatrix {

	protected String name;
	
	protected String[] colNames;
	protected String[] rowNames;
	
	protected DoubleMatrix2D data;
	
	public DoubleMatrix() {
		this.name = "";
	}

	public DoubleMatrix(
			String name, String[] colNames, 
			String[] rowNames, DoubleMatrix2D data) {
		
		this.name = name;
		this.colNames = colNames;
		this.rowNames = rowNames;
		this.data = data;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public final String[] getColNames() {
		return colNames;
	}

	public final void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	@XmlTransient
	public final String[] getRowNames() {
		return rowNames;
	}

	public final void setRowNames(String[] rowNames) {
		this.rowNames = rowNames;
	}

	@XmlTransient
	public final DoubleMatrix2D getData() {
		return data;
	}

	public final void setData(DoubleMatrix2D data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name).append('\n');
		sb.append(colNames).append('\n');
		sb.append(rowNames).append('\n');
		sb.append(data).append('\n');
		
		return sb.toString();
	}
}

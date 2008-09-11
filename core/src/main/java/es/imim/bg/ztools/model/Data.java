package es.imim.bg.ztools.model;

import java.util.ArrayList;
import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;

@Deprecated
public class Data {

	private String dataName;
	private List<String> colNames;
	private List<String> rowNames;
	private DoubleMatrix2D data;
	
	public Data() {
		this.dataName = "";
		this.colNames = new ArrayList<String>();
		this.rowNames = new ArrayList<String>();
	}

	public Data(
			String dataName, List<String> colNames,
			List<String> rowNames, DoubleMatrix2D data) {
		this.dataName = dataName;
		this.colNames = colNames;
		this.rowNames = rowNames;
		this.data = data;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	
	public String getDataName() {
		return dataName;
	}
	
	public void setColNames(List<String> colNames) {
		this.colNames = colNames;
	}
	
	public List<String> getColNames() {
		return colNames;
	}
	
	public void setRowNames(List<String> rowNames) {
		this.rowNames = rowNames;
	}
	
	public List<String> getRowNames() {
		return rowNames;
	}
	
	public void setData(DoubleMatrix2D data) {
		this.data = data;
	}
	
	public DoubleMatrix2D getData() {
		return data;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(dataName).append('\n');
		sb.append(colNames).append('\n');
		sb.append(rowNames).append('\n');
		sb.append(data).append('\n');
		
		return sb.toString();
	}
}

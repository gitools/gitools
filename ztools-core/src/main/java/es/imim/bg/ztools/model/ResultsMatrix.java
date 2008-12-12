package es.imim.bg.ztools.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix3D;
import es.imim.bg.ztools.test.results.Result;

@XmlType(
		propOrder = {
				"resultClass",
				/*"colNames", 
				"rowNames",*/ 
				"paramNames"/*,
				"data"*/})
				
public class ResultsMatrix {

	protected Class<? extends Result> resultClass;
	
	protected String[] colNames;
	protected String[] rowNames;
	protected String[] paramNames;
	protected Map<String, Integer> paramNameToIndex;
	
	protected DoubleMatrix3D data;

	public ResultsMatrix() {
	}
	
	public ResultsMatrix(
			String[] colNames, 
			String[] rowNames, 
			String[] paramNames,
			DoubleMatrix3D data) {
		
		this.colNames = colNames;
		this.rowNames = rowNames;
		this.paramNames = paramNames;
		this.data = data;

		initParamNamesToIndexMap();
	}

	private void initParamNamesToIndexMap() {
		if (paramNameToIndex == null)
			paramNameToIndex = new HashMap<String, Integer>();
		else
			paramNameToIndex.clear();
		
		for (int index = 0; index < paramNames.length; index++)
			paramNameToIndex.put(paramNames[index], index);
	}

	public void makeData() {
		data = DoubleFactory3D.dense.make(
				paramNames.length,
				rowNames.length,
				colNames.length
				);
	}
	
	public Class<? extends Result> getResultClass() {
		return resultClass;
	}
	
	public void setResultClass(Class<? extends Result> resultClass) {
		this.resultClass = resultClass;
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

	@XmlElementWrapper(name = "statistics")
	@XmlElement(name = "statistic")
	//@XmlAttribute(name = "name")
	public final String[] getParamNames() {
		return paramNames;
	}

	public final void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
		initParamNamesToIndexMap();
	}

	@XmlTransient
	public final DoubleMatrix3D getData() {
		return data;
	}

	public final void setData(DoubleMatrix3D data) {
		this.data = data;
	}
	
	@XmlTransient
	public final int getParamCount() {
		return data.slices();
	}

	@XmlTransient
	public final int getRowCount() {
		return data.rows();
	}
	
	@XmlTransient
	public final int getColumnCount() {
		return data.columns();
	}
	
	@XmlTransient //FIXME: Really needed ?
	public final String getRowName(int index) {
		return rowNames[index];
	}
	
	@XmlTransient //FIXME: Really needed ?
	public final String getColumnName(int index) {
		return colNames[index];
	}
	
	public double getDataValue(int colIndex, int rowIndex, String paramName) {
		final int paramIndex = paramNameToIndex.get(paramName);
		return data.getQuick(paramIndex, rowIndex, colIndex);
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

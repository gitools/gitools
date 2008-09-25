package es.imim.bg.ztools.ui.model;

import es.imim.bg.ztools.model.Results;

public class ResultsModel {

	private Results results;
	
	public ResultsModel(Results results) {
		this.results = results;
	}
	
	/*public Results getResults() {
		return results;
	}*/

	public int getColumnCount() {
		return results.getColNames().length;
	}

	public String getColumnName(int column) {
		return results.getColNames()[column];
	}

	public int getRowCount() {
		return results.getRowNames().length;
	}

	public String getRowName(int row) {
		return results.getRowNames()[row];
	}

	public int getParamCount() {
		return results.getParamNames().length;
	}
	
	public String getParamName(int param) {
		return results.getParamNames()[param];
	}
	
	public Double getValue(int column, int row, int param) {
		return results.getDataValue(column, row, param);
	}

	public String[] getParamNames() {
		return results.getParamNames();
	}

	public int getParamIndex(String name) {
		return results.getParamIndex(name);
	}
}

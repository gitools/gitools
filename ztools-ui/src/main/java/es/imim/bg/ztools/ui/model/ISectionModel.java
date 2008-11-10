package es.imim.bg.ztools.ui.model;

import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;

public interface ISectionModel extends ITableModel {

	String SELECTION_PARAM_PROPERTY = "selectionParam";
	
	public enum SectionLayout {
		left, right, top, bottom
	}
	
	String getName();
	ITableModel getTableModel();
	SectionLayout getLayout();
	
	int getParamCount();
	
	String getParamName(int param);
	
	String[] getParamNames();
	
	double getValue(int column, int row, int param);
	
	void setSelectedParam(int param);
	void setSelectedParam(String paramName);
	
	int getSelectedParam();
	
	CellDecorationConfig getParamDecoration(int param);
}

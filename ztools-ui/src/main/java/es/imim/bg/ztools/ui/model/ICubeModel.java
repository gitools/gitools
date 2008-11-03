package es.imim.bg.ztools.ui.model;

public interface ICubeModel extends ITableModel {

	int getParamCount();
	
	String getParamName(int param);
	
	String[] getParamNames();
	
	double getValue(int column, int row, int param);
	
	void setSelectedParam(int param);
	
	int getSelectedParam();
}

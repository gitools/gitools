package es.imim.bg.ztools.zcalc.ui.colormatrix;

public interface ColorMatrixModel {

	int getRowCount();
	String getRowName(int row);
	
	int getColumnCount();
	String getColumnName(int column);

	Double getValue(int row, int column);
}

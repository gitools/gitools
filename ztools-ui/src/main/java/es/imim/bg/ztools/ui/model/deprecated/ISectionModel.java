package es.imim.bg.ztools.ui.model.deprecated;

import cern.colt.matrix.DoubleMatrix2D;
import es.imim.bg.ztools.ui.model.IModel;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.panels.table.CellDecorationConfig;

@Deprecated
public interface ISectionModel extends IModel {

	String SELECTION_PARAM_PROPERTY = "selectionParam";
	
	String PARAM_COUNT_CHANGED_PROPERTY = "paramCount";
	
	public enum SectionLayout {
		left, right, top, bottom
	}
	
	String getName();
	
	SectionLayout getLayout();
	
	ITableModel getTable();
	
	int getTableCount();
	
	String getTableName(int param);
	
	String[] getTableNames();
	
	double getValue(int column, int row, int param);
	
	int getCurrentTable();
	void setCurrentTable(int param);
	void setCurrentTable(String paramName);
	
	int addTable(String paramName, DoubleMatrix2D correctedMatrix);
	
	CellDecorationConfig getTableDecoration(int param);
}

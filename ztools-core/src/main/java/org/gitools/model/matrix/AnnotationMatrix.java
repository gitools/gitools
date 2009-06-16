package org.gitools.model.matrix;

import java.util.HashMap;
import java.util.Map;

import org.gitools.model.matrix.element.IElementAdapter;

import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;


public class AnnotationMatrix extends StringMatrix {

	private static final long serialVersionUID = 2941738380629859631L;

	private Map<String, Integer> rowMap;
	private Map<String, Integer> colMap;
	
	public AnnotationMatrix() {
		super();
	}
	
	public AnnotationMatrix(
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells,
			IElementAdapter rowAdapter,
			IElementAdapter columnAdapter,
			IElementAdapter cellAdapter) {
	    
		super(rows, columns, cells, rowAdapter, columnAdapter, cellAdapter);
		
		updateRowsMap();
		updateColumnsMap();
	}
	
	@Override
	public void setRows(ObjectMatrix1D rows) {
		super.setRows(rows);
		updateRowsMap();
	}
	
	@Override
	public void setColumns(ObjectMatrix1D columns) {
		super.setColumns(columns);
		updateColumnsMap();
	}
	
	public int getRowIndex(String id) {
		int index = -1;
		Integer idx = rowMap.get(id);
		if (idx != null)
			index = idx.intValue();
		return index;
	}
	
	public int getColumnIndex(String id) {
		int index = -1;
		Integer idx = colMap.get(id);
		if (idx != null)
			index = idx.intValue();
		return index;
	}
	
	private void updateRowsMap() {
		if (rowMap == null)
			rowMap = new HashMap<String, Integer>();
		
		rowMap.clear();
		
		for (int i = 0; i < rows.size(); i++)
			rowMap.put(rows.getQuick(i).toString(), i);
	}
	
	private void updateColumnsMap() {
		if (colMap == null)
			colMap = new HashMap<String, Integer>();
		
		colMap.clear();
		
		for (int i = 0; i < columns.size(); i++)
			colMap.put(columns.getQuick(i).toString(), i);
	}
}

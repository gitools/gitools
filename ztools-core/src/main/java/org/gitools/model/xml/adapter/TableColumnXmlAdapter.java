package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.impl.AnnotationTableColumn;
import org.gitools.model.table.impl.MatrixCellTableColumn;
import org.gitools.model.table.impl.MatrixPropertyTableColumn;


public class TableColumnXmlAdapter extends XmlAdapter<String, ITableColumn> {
	@Override
	public String marshal(ITableColumn v) throws Exception {
		if (v instanceof AnnotationTableColumn)
			return "AnnotationTableColumn";
		if (v instanceof MatrixCellTableColumn)
			return "MatrixCellTableColumn";	
		if (v instanceof MatrixPropertyTableColumn)
			return "MatrixPropertyTableColumn";	
		return null;
	}
	@Override
	public ITableColumn unmarshal(String v) throws Exception {
		if (v.equals("AnnotationTableColumn"))
			return (ITableColumn) new AnnotationTableColumn();
		if (v.equals("MatrixCellTableColumn"))
			return (ITableColumn) new MatrixCellTableColumn();
		if (v.equals("MatrixCellTableColumn"))
			return (ITableColumn) new MatrixCellTableColumn();
		return null;
	
	
}
	}

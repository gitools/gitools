package org.gitools.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.table.model.ITableColumn;
import org.gitools.table.model.impl.AbstractTableColumn;

//FIXME Review
public class TableColumnXmlAdapter extends XmlAdapter<AbstractTableColumn, ITableColumn> {

	@Override
	public AbstractTableColumn marshal(ITableColumn table) throws Exception {
		return (AbstractTableColumn) table;
	}

	@Override
	public ITableColumn unmarshal(AbstractTableColumn table) throws Exception {
		return (ITableColumn) table;
	}
}

package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.impl.AbstractTableColumn;

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

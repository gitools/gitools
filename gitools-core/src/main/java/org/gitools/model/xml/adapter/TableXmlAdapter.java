package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.table.model.ITable;
import org.gitools.table.model.Table;

//FIXME Review
public class TableXmlAdapter extends XmlAdapter<Table, ITable> {

	@Override
	public Table marshal(ITable v) throws Exception {
		return (Table) v;
	}

	@Override
	public ITable unmarshal(Table v) throws Exception {
		return v;
	}

}

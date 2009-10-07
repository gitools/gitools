package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.table.ITable;
import org.gitools.model.table.Table;

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

package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.table.ITableColumn;
import org.gitools.model.xml.TableColumnXmlElement;
import org.gitools.resources.factory.ResourceFactory;

public class TableColumnXmlAdapter extends XmlAdapter<TableColumnXmlElement, ITableColumn> {

	ResourceFactory resourceFactory;
	public TableColumnXmlAdapter(){
	
	}
	public TableColumnXmlAdapter(ResourceFactory resourceFactory){
		this.resourceFactory = resourceFactory;
	}
	@Override
	public TableColumnXmlElement marshal(ITableColumn column) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ITableColumn unmarshal(TableColumnXmlElement elem) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

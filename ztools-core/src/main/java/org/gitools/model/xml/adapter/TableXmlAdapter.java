package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.model.table.ITable;
import org.gitools.model.table.Table;
import org.gitools.model.xml.MatrixXmlElement;
import org.gitools.persistence.Extensions;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.factory.ResourceFactory;

public class TableXmlAdapter extends XmlAdapter<MatrixXmlElement, ITable> {

	ResourceFactory resourceFactory;
	public TableXmlAdapter(){
	
	}
	public TableXmlAdapter(ResourceFactory resourceFactory){
		this.resourceFactory = resourceFactory;
	}
	
	
	@Override
	public MatrixXmlElement marshal(ITable v) throws Exception {
		Table table = (Table) v;
		return new MatrixXmlElement(Extensions.getEntityExtension(v
				.getClass()), table.getResource());
	}

	@Override
	public ITable unmarshal(MatrixXmlElement v) throws Exception {
		ITable table = (ITable) PersistenceManager.load(resourceFactory, v.getReference(), (v.getType()));
		return table;
	}
}

package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.Matrix;
import org.gitools.model.xml.MatrixXmlElement;
import org.gitools.persistence.Extensions;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.factory.ResourceFactory;

public class MatrixXmlAdapter extends XmlAdapter<MatrixXmlElement, IMatrix> {

	ResourceFactory resourceFactory;
	public MatrixXmlAdapter(){
		
	}
	
	
	public MatrixXmlAdapter(ResourceFactory resourceFactory){
		this.resourceFactory = resourceFactory;
	}
	
	
	@Override
	public MatrixXmlElement marshal(IMatrix v) throws Exception {
		Matrix matrix = (Matrix) v;
		return new MatrixXmlElement(Extensions.getEntityExtension(v
				.getClass()), matrix.getResource());
	}

	@Override
	public IMatrix unmarshal(MatrixXmlElement v) throws Exception {
		IMatrix matrix = (IMatrix) PersistenceManager.load(resourceFactory, v.getReference(), (v.getType()));
		return matrix;
	}
}

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

	public MatrixXmlAdapter(ResourceFactory resourceFactory){
		this.resourceFactory = resourceFactory;
	}
	
	
	@Override
	public MatrixXmlElement marshal(IMatrix v) throws Exception {
		if (v== null) return null; 
		Matrix matrix = (Matrix) v;
		return new MatrixXmlElement(matrix.getResource(), matrix);
	}

	@Override
	public IMatrix unmarshal(MatrixXmlElement v) throws Exception {
		Matrix contents = v.getMatrix();
		String extension = Extensions.getEntityExtension(contents.getClass());
		Matrix matrix =  (Matrix) PersistenceManager.load(resourceFactory, v.getReference(),extension);
		matrix.setTitle(contents.getTitle());
		matrix.setDescription(contents.getDescription());
		return matrix;
	}
}

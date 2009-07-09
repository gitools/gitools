package org.gitools.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.persistence.FileExtensions;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.IResource;

public class MatrixXmlAdapter extends XmlAdapter<MatrixXmlElement, IMatrix> {

	IResource resource;

	public MatrixXmlAdapter() {
	}

	public MatrixXmlAdapter(IResource matrixResource) {
		this.resource = matrixResource;
	}

	@Override
	public MatrixXmlElement marshal(IMatrix v) throws Exception {
		return new MatrixXmlElement(FileExtensions.getEntityExtension(v
				.getClass()), resource);
	}

	@Override
	public IMatrix unmarshal(MatrixXmlElement v) throws Exception {
		IMatrix matrix = (IMatrix) PersistenceManager.load(null, v.getReference(),(v.getType()));
		return matrix;
	}
}

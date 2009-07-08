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
		return (IMatrix) PersistenceManager.load(null, v.getReference(),
				FileExtensions.getEntityExtension(v.getClass()));
	}

	/*
	 * 
	 * public String marshal(IMatrix v) throws Exception { // FIXME: de donde
	 * puedo sacar el resource para guardar la matriz? return
	 * resource.toURI().toString().replace( baseResource.toURI().toString(),
	 * ""); }
	 * 
	 * public IMatrix unmarshal(String v) throws Exception { // FIXME: el
	 * baseResource no es necesario siempre no hay porque // arrastralo
	 * 
	 * URI uri = new URI(v); URI path = resource.toURI().resolve(uri);
	 * 
	 * return (IMatrix) PersistenceManager.load(null, new FileResource( new
	 * File(path)), FileExtensions.OBJECT_MATRIX);
	 */
}

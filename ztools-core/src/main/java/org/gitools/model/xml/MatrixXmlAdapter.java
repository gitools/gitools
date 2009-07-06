package org.gitools.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.FileResource;

public class MatrixXmlAdapter extends XmlAdapter <FileResource,IMatrix> {
	FileResource matrixResource;
	
	public MatrixXmlAdapter (FileResource resource){
		this.matrixResource = resource;
	}

	@Override
	public FileResource marshal(IMatrix v) throws Exception {
		//FIXME: de donde puedo sacar el resource para guardar la matriz?
		return matrixResource;
	}

	@Override
	public IMatrix unmarshal(FileResource v) throws Exception {
		//FIXME: el baseResource no es necesario siempre no  hay porque arrastralo
		return (IMatrix) PersistenceManager.load(null, v);
	}

}

package org.gitools.model.xml;

import java.io.File;
import java.net.URI;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

public class MatrixXmlAdapter extends XmlAdapter <String,IMatrix> {
	
	IResource baseResource;
	FileResource resource;
	
	public MatrixXmlAdapter(){
	}

	public MatrixXmlAdapter (FileResource resource, IResource baseResource){
		this.baseResource = baseResource;
		this.resource = resource;
	}

	@Override
	public String marshal(IMatrix v) throws Exception {
		//FIXME: de donde puedo sacar el resource para guardar la matriz?
		return resource.toURI().toString().replace(
				baseResource.toURI().toString(), "");
	}

	public IMatrix unmarshal(String v) throws Exception {
		//FIXME: el baseResource no es necesario siempre no  hay porque arrastralo
		URI uri = new URI(v);
		URI path = resource.toURI().resolve(uri);
		return (IMatrix) PersistenceManager.load(null, new FileResource(new File(path)));
	}

}

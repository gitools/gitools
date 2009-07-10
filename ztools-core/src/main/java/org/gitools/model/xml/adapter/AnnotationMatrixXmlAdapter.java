package org.gitools.model.xml.adapter;

import java.io.File;
import java.net.URI;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.persistence.FileExtensions;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

public class AnnotationMatrixXmlAdapter extends
		XmlAdapter<String, AnnotationMatrix> {

	IResource baseResource;
	
	public AnnotationMatrixXmlAdapter() {
	
	}

	public AnnotationMatrixXmlAdapter(FileResource resource,
			IResource baseResource) {
		this.baseResource = baseResource;
	}

	@Override
	public String marshal(AnnotationMatrix v) throws Exception {
		// FIXME: de donde puedo sacar el resource para guardar la matriz?
		return v.getResource().toURI().toString().replace(
				baseResource.toURI().toString(), "");
	}

	public AnnotationMatrix unmarshal(String v) throws Exception {
		// FIXME: el baseResource no es necesario siempre no hay porque
		// arrastralo

		URI uri = new URI(v);
		URI path = baseResource.toURI().resolve(uri);

		return (AnnotationMatrix) PersistenceManager.load(
				new FileResource(new File(path)),
				FileExtensions.ANNOTATION_MATRIX);

	}

}

package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.persistence.ResourceNameSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.factory.ResourceFactory;

public class AnnotationMatrixXmlAdapter extends
		XmlAdapter<String, AnnotationMatrix> {

	ResourceFactory resourceFactory;
	
	public AnnotationMatrixXmlAdapter(ResourceFactory resourceFactory) {
		this.resourceFactory = resourceFactory;
	}

	@Override
	public String marshal(AnnotationMatrix v) throws Exception {
		if(v == null) return null;
		return v.getResource().toString();
	}

	public AnnotationMatrix unmarshal(String v) throws Exception {
		return (AnnotationMatrix) PersistenceManager.load(resourceFactory,
				resourceFactory.getResource(v), ResourceNameSuffixes.ANNOTATION_MATRIX);

	}

}

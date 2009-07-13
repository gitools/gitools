package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.persistence.Extensions;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.IResource;
import org.gitools.resources.ProjectResource;

public class AnnotationMatrixXmlAdapter extends
		XmlAdapter<String, AnnotationMatrix> {

	IResource base;
	
	public AnnotationMatrixXmlAdapter() {
	
	}

	public AnnotationMatrixXmlAdapter(ProjectResource resource) {
		this.base= resource.getBase();
	}

	@Override
	public String marshal(AnnotationMatrix v) throws Exception {
		return v.getResource().toString();
	}

	public AnnotationMatrix unmarshal(String v) throws Exception {
		return (AnnotationMatrix) PersistenceManager.load(
				new ProjectResource(base,v),Extensions.ANNOTATION_MATRIX);

	}

}

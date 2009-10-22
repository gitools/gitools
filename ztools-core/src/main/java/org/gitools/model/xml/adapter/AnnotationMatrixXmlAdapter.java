package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.persistence.IPathResolver;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.FileSuffixes;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

//FIXME Review
public class AnnotationMatrixXmlAdapter
		extends XmlAdapter<String, AnnotationMatrix> {

	private IPathResolver pathResolver;
	
	public AnnotationMatrixXmlAdapter(IPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

	@Override
	public String marshal(AnnotationMatrix v) throws Exception {
		if(v == null) return null;
		//FIXME Artifact.getResource()
		//return v.getResource().toString();
		return null;
	}

	public AnnotationMatrix unmarshal(String v) throws Exception {
		return (AnnotationMatrix) PersistenceManager.getDefault().load(
				pathResolver,
				pathResolver.createResourceFromString(v),
				FileSuffixes.ANNOTATION_MATRIX,
				new NullProgressMonitor());
	}

}

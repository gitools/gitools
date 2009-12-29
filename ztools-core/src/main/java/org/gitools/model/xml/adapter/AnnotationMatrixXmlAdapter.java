package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.persistence.IPathResolver;
import org.gitools.persistence.PersistenceManager;

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
		return PersistenceManager.getDefault().getEntityFile(v).toString();
		}
	
	@Override
	public AnnotationMatrix unmarshal(String v) throws Exception {
		AnnotationMatrix annotations =  (AnnotationMatrix) PersistenceManager.getDefault().load(
				pathResolver, pathResolver.createResourceFromString(v), null, new NullProgressMonitor());

		return annotations;
	}
}

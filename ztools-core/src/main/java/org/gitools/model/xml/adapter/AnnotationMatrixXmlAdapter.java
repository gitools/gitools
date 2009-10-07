package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.persistence.FileObjectResolver;
import org.gitools.persistence.IFileObjectResolver;
import org.gitools.persistence.ResourceNameSuffixes;
import org.gitools.persistence.PersistenceManager;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

//FIXME Review
public class AnnotationMatrixXmlAdapter
		extends XmlAdapter<String, AnnotationMatrix> {

	private IFileObjectResolver fileObjectResolver;
	
	public AnnotationMatrixXmlAdapter(IFileObjectResolver fileObjectResolver) {
		this.fileObjectResolver = fileObjectResolver;
	}

	@Override
	public String marshal(AnnotationMatrix v) throws Exception {
		if(v == null) return null;
		return v.getResource().toString();
	}

	public AnnotationMatrix unmarshal(String v) throws Exception {
		return (AnnotationMatrix) PersistenceManager.load(fileObjectResolver,
				fileObjectResolver.createResourceFromString(v),
				ResourceNameSuffixes.ANNOTATION_MATRIX,
				new NullProgressMonitor());

	}

}

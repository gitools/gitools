package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.Matrix;
import org.gitools.model.xml.MatrixXmlElement;
import org.gitools.persistence.IPathResolver;
import org.gitools.persistence.PersistenceManager;

import edu.upf.bg.progressmonitor.NullProgressMonitor;


public class MatrixXmlAdapter extends XmlAdapter<MatrixXmlElement, IMatrix> {

	IPathResolver pathResolver;

	public MatrixXmlAdapter(IPathResolver pathResolver){
		this.pathResolver = pathResolver;
	}
	
	
	@Override
	public MatrixXmlElement marshal(IMatrix v) throws Exception {
		return new MatrixXmlElement(PersistenceManager.getDefault().getEntityFile(v));
	}

	@Override
	public IMatrix unmarshal(MatrixXmlElement v) throws Exception {
		return (Matrix) PersistenceManager.getDefault().load(
				pathResolver, v.getResource(), null, new NullProgressMonitor());
	}
}

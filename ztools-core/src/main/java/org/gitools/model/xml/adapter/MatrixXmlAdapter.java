package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.Matrix;
import org.gitools.model.xml.MatrixXmlElement;
import org.gitools.persistence.IPathResolver;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.FileSuffixes;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

//FIXME Review: calculon ????
public class MatrixXmlAdapter extends XmlAdapter<MatrixXmlElement, IMatrix> {

	IPathResolver pathResolver;

	public MatrixXmlAdapter(IPathResolver pathResolver){
		this.pathResolver = pathResolver;
	}
	
	
	@Override
	public MatrixXmlElement marshal(IMatrix v) throws Exception {
		if (v== null) return null; 
		Matrix matrix = (Matrix) v;
		matrix.setTitle("calculon");
		matrix.setDescription("description");
		return new MatrixXmlElement(matrix);
	}

	@Override
	public IMatrix unmarshal(MatrixXmlElement v) throws Exception {
		Matrix contents = v.getMatrix();
		String extension = FileSuffixes.getEntityExtension(contents.getClass());
		//FIXME Artifact.getResource()
		/*Matrix matrix =  (Matrix) PersistenceManager.load(fileObjectResolver,
				contents.getResource(), extension,
				new NullProgressMonitor());
		matrix.setTitle(contents.getTitle());
		matrix.setDescription(contents.getDescription());
		return matrix;*/
		return null;
	}
}

package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.Matrix;
import org.gitools.model.table.Table;
import org.gitools.model.xml.MatrixXmlElement;
import org.gitools.model.xml.TableXmlElement;
import org.gitools.persistence.Extensions;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.factory.ResourceFactory;

public class TableXmlAdapter extends XmlAdapter<TableXmlElement, Table> {

	ResourceFactory resourceFactory;
	
	public TableXmlAdapter(ResourceFactory resourceFactory){
		this.resourceFactory = resourceFactory;
	}
	
	
	@Override
	public TableXmlElement marshal(Table v) throws Exception {
		Table table = v;
		Matrix matrix  = (Matrix) v.getMatrix();
		AnnotationMatrix annotations =	v.getAnnotations();
		
		MatrixXmlElement matrixElement = new MatrixXmlElement(
				Extensions.getEntityExtension(matrix.getClass()), matrix.getResource());
		
		MatrixXmlElement annotationsElement = new MatrixXmlElement(
				Extensions.getEntityExtension(annotations.getClass()), annotations.getResource());
		
		
		return new TableXmlElement(matrixElement, annotationsElement);
	}

	@Override
	public Table unmarshal(TableXmlElement v) throws Exception {
		IMatrix  matrix =  (IMatrix) PersistenceManager.load(resourceFactory, v.getMatrix().getReference(), (v.getMatrix().getType()));
		AnnotationMatrix  annotations =  (AnnotationMatrix) PersistenceManager.load(resourceFactory, v.getAnnotations().getReference(), (v.getAnnotations().getType()));
	 return new Table((Matrix) matrix, annotations);
		
		
	}
}

package org.gitools.persistence.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.FileXmlAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;

public class MatrixFigureXmlPersistence
		extends AbstractXmlPersistence<MatrixFigure> {

	public MatrixFigureXmlPersistence() {
		super(MatrixFigure.class);
	}

	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter[] {
				new FileXmlAdapter(pathResolver),
				new AnnotationMatrixXmlAdapter(pathResolver),
				new MatrixXmlAdapter(pathResolver) };
	}
}

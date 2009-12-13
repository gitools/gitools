package org.gitools.persistence.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.FileXmlAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;

public class MatrixFigureXmlPersistence
		extends AbstractXmlPersistence<Heatmap> {

	public MatrixFigureXmlPersistence() {
		super(Heatmap.class);
	}

	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter[] {
				new FileXmlAdapter(pathResolver),
				new AnnotationMatrixXmlAdapter(pathResolver),
				new MatrixXmlAdapter(pathResolver) };
	}
}

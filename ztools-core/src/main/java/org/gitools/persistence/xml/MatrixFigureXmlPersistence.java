package org.gitools.persistence.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.resources.factory.ResourceFactory;

public class MatrixFigureXmlPersistence extends AbstractXmlPersistence {

	public MatrixFigureXmlPersistence(
			ResourceFactory resourceFactory,
			Class<?> entityClass) throws JAXBException {
		
		super(entityClass);

		//IResource base = null;

		setAdapters(new XmlAdapter[] {
				new ResourceXmlAdapter(resourceFactory),
				new AnnotationMatrixXmlAdapter(resourceFactory),
				new MatrixXmlAdapter(resourceFactory)
		});
	}

}

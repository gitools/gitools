package org.gitools.persistence;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.resources.factory.ResourceFactory;

public class XmlMatrixFigurePersistence extends XmlGenericPersistence {

	public XmlMatrixFigurePersistence(ResourceFactory resourceFactory,
			Class<?> entityClass) {
		super(entityClass);

		super.adapters = new XmlAdapter[] {
				new ResourceXmlAdapter(resourceFactory),
				new AnnotationMatrixXmlAdapter(resourceFactory) };
	}

}

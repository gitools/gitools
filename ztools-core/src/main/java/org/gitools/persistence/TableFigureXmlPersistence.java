package org.gitools.persistence;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.model.xml.adapter.TableXmlAdapter;
import org.gitools.resources.factory.ResourceFactory;

public class TableFigureXmlPersistence extends XmlGenericPersistence {

	@SuppressWarnings("unchecked")
	public TableFigureXmlPersistence(ResourceFactory resourceFactory,
			Class<?> entityClass) throws JAXBException {
		super(entityClass);


		XmlAdapter[] adapters = new XmlAdapter[3];
		adapters[0] = new ResourceXmlAdapter(resourceFactory);
		adapters[1] = new AnnotationMatrixXmlAdapter(resourceFactory);
		adapters[2] = new TableXmlAdapter(resourceFactory);
		super.adapters = adapters;

		
	}

}

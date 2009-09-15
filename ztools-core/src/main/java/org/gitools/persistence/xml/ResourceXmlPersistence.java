package org.gitools.persistence.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.resources.factory.ResourceFactory;

public class ResourceXmlPersistence extends AbstractXmlPersistence {

	public ResourceXmlPersistence(
			ResourceFactory resourceFactory,
			Class<?> entityClass) throws JAXBException {
		
		super(entityClass);

		setAdapters(new XmlAdapter[] {
				new ResourceXmlAdapter(resourceFactory) });
	}

}

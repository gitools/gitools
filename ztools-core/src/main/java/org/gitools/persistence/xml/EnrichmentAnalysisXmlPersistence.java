package org.gitools.persistence.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.AnnotationMatrixXmlAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.ModuleMapXmlAdapter;
import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.resources.factory.ResourceFactory;

public class EnrichmentAnalysisXmlPersistence extends AbstractXmlPersistence {

	@SuppressWarnings("unchecked")
	public EnrichmentAnalysisXmlPersistence(ResourceFactory resourceFactory,
			Class<?> entityClass) throws JAXBException {
		super(entityClass);

		XmlAdapter[] adapters = new XmlAdapter[4];
		adapters[0] = new ResourceXmlAdapter(resourceFactory);
		adapters[1] = new AnnotationMatrixXmlAdapter(resourceFactory);
		adapters[2] = new MatrixXmlAdapter(resourceFactory);
		adapters[3] = new ModuleMapXmlAdapter(resourceFactory);
		
		setAdapters(adapters);
	}

}

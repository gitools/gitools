package org.gitools.persistence.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.Container;
import org.gitools.model.xml.adapter.FileObjectXmlAdapter;

public class ContainerXmlPersistence
		extends AbstractXmlPersistence<Container> {

	public ContainerXmlPersistence() {	
		super(Container.class);
	}
	
	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter[] {
				new FileObjectXmlAdapter(fileObjectResolver) };
	}

}

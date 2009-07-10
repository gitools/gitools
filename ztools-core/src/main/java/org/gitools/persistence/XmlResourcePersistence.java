package org.gitools.persistence;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.resources.IResource;

public class XmlResourcePersistence extends XmlGenericPersistence{

	public XmlResourcePersistence(
			IResource baseResource, Class<?> entityClass) {
		
		super(entityClass);

		super.adapters = new XmlAdapter[] {
				new ResourceXmlAdapter(baseResource) };
		}

}



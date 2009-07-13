package org.gitools.persistence;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.resources.IResource;
import org.gitools.resources.ProjectResource;

public class XmlResourcePersistence extends XmlGenericPersistence {

	public XmlResourcePersistence(IResource resource, Class<?> entityClass) {
		super(entityClass);

		IResource base = null;

		if (resource instanceof ProjectResource)
			base = ((ProjectResource) resource).getBase();

		super.adapters = new XmlAdapter[] { new ResourceXmlAdapter(base) };
	}

}

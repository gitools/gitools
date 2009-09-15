package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.ModuleMap;
import org.gitools.persistence.ResourceNameSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.resources.factory.ResourceFactory;

public class ModuleMapXmlAdapter extends XmlAdapter <String, ModuleMap>{

	ResourceFactory resourceFactory;
	
	public ModuleMapXmlAdapter(ResourceFactory resourceFactory){
		this.resourceFactory = resourceFactory;
	}
	
	@Override
	public String marshal(ModuleMap v) throws Exception {
		if( v== null) return null;
		return v.getResource().toString();
	}

	@Override
	public ModuleMap unmarshal(String v) throws Exception {
		return (ModuleMap) PersistenceManager.load(resourceFactory,
				resourceFactory.getResource(v), ResourceNameSuffixes.MODULE_MAP);
	}

}

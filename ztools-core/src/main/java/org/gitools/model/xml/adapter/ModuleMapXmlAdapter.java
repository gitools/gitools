package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.ModuleMap;
import org.gitools.persistence.IFileObjectResolver;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceNameSuffixes;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

public class ModuleMapXmlAdapter extends XmlAdapter <String, ModuleMap>{

	IFileObjectResolver fileObjectResolver;
	
	public ModuleMapXmlAdapter(IFileObjectResolver fileObjectResolver){
		this.fileObjectResolver = fileObjectResolver;
	}
	
	@Override
	public String marshal(ModuleMap v) throws Exception {
		if( v== null) return null;
		return v.getResource().toString();
	}

	@Override
	public ModuleMap unmarshal(String v) throws Exception {
		return (ModuleMap) PersistenceManager.load(fileObjectResolver,
				fileObjectResolver.createResourceFromString(v),
				ResourceNameSuffixes.MODULE_MAP,
				new NullProgressMonitor());
	}

}

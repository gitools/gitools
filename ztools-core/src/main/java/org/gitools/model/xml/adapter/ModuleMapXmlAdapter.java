package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.ModuleMap;
import org.gitools.persistence.IPathResolver;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.FileSuffixes;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

public class ModuleMapXmlAdapter extends XmlAdapter <String, ModuleMap>{

	IPathResolver pathResolver;
	
	public ModuleMapXmlAdapter(IPathResolver pathResolver){
		this.pathResolver = pathResolver;
	}
	
	@Override
	public String marshal(ModuleMap v) throws Exception {
		if( v== null) return null;
		//FIXME Artifact.getResource()
		//return v.getResource().toString();
		return null;
	}

	@Override
	public ModuleMap unmarshal(String v) throws Exception {
		return (ModuleMap) PersistenceManager.getDefault().load(
				pathResolver,
				pathResolver.createResourceFromString(v),
				FileSuffixes.MODULE_MAP,
				new NullProgressMonitor());
	}

}

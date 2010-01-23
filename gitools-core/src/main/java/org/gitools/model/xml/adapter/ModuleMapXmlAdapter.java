package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.ModuleMap;
import org.gitools.persistence.IPathResolver;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.FileSuffixes;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import java.io.File;
import org.gitools.persistence.PersistenceUtils;

public class ModuleMapXmlAdapter extends XmlAdapter <String, ModuleMap>{

	IPathResolver pathResolver;
	
	public ModuleMapXmlAdapter(IPathResolver pathResolver){
		this.pathResolver = pathResolver;
	}
	
	@Override
	public String marshal(ModuleMap v) throws Exception {
		if(v == null)
			return null;

		File file = PersistenceManager.getDefault().getEntityFile(v);
		String relativePath = PersistenceUtils.getRelativePath(
				pathResolver.pathToString(), file.getAbsolutePath());
		return relativePath;
	}

	@Override
	public ModuleMap unmarshal(String v) throws Exception {
		return (ModuleMap) PersistenceManager.getDefault().load(
				pathResolver,
				pathResolver.stringToPath(v),
				FileSuffixes.MODULE_MAP,
				new NullProgressMonitor());
	}
}

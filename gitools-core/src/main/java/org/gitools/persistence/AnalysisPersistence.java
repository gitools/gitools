package org.gitools.persistence;

import org.gitools.model.Analysis;
import org.gitools._DEPRECATED.resources.FileResource;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public abstract class AnalysisPersistence extends FileResource {

	private static final long serialVersionUID = -5243387650958406472L;
	
	protected String basePath;
	
	public AnalysisPersistence(String basePath) {
		super();
		this.basePath = basePath;
		//new File(basePath, name + File.separator + "analysis.xml")
	}
	
	public abstract Analysis load(IProgressMonitor monitor) 
			throws PersistenceException;
	
	public abstract void save(Analysis analysis, IProgressMonitor monitor) 
			throws PersistenceException;

}

package org.gitools.persistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.HtestAnalysis;

public abstract class AnalysisPersistence {// extends FileResource {

	private static final long serialVersionUID = -5243387650958406472L;
	
	protected String basePath;
	
	public AnalysisPersistence(String basePath) {
		super();
		this.basePath = basePath;
		//new File(basePath, name + File.separator + "analysis.xml")
	}
	
	public abstract HtestAnalysis load(IProgressMonitor monitor)
			throws PersistenceException;
	
	public abstract void save(HtestAnalysis analysis, IProgressMonitor monitor)
			throws PersistenceException;

}

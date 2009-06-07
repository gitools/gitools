package org.gitools.resources.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.gitools.model.Analysis;
import org.gitools.resources.Resource;

import edu.upf.bg.progressmonitor.ProgressMonitor;

public abstract class AnalysisResource extends Resource {

	private static final long serialVersionUID = -5243387650958406472L;
	
	protected String basePath;
	
	public AnalysisResource(String basePath) {
		super();
		this.basePath = basePath;
		//new File(basePath, name + File.separator + "analysis.xml")
	}
	
	public abstract Analysis load(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException;
	
	public abstract void save(Analysis analysis, ProgressMonitor monitor) 
			throws IOException, DataFormatException;

}

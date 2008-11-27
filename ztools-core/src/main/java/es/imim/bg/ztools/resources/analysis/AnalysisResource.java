package es.imim.bg.ztools.resources.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.resources.Resource;

public abstract class AnalysisResource extends Resource {
	
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

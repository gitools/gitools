package es.imim.bg.ztools.resources.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;

public class XmlAnalysisResource extends AnalysisResource {

	protected boolean resultsOrderByCond;
	
	public XmlAnalysisResource(String basePath, boolean resultsOrderByCond) {
		super(basePath);
		this.resultsOrderByCond = resultsOrderByCond;
	}
	
	public XmlAnalysisResource(String basePath) {
		this(basePath, true);
	}

	@Override
	public Analysis load(ProgressMonitor monitor) throws FileNotFoundException, IOException,
			DataFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Analysis analysis, ProgressMonitor monitor) throws IOException, DataFormatException {
		// TODO Auto-generated method stub

	}

}

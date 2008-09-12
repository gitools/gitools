package es.imim.bg.ztools.resources.analysis;

import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.ztools.model.Analysis;

public interface AnalysisResource {
	
	public void save(Analysis analysis) throws IOException, DataFormatException;

}

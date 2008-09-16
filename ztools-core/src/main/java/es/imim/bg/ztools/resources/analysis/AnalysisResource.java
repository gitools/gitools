package es.imim.bg.ztools.resources.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.ztools.model.Analysis;

public interface AnalysisResource {
	
	public Analysis load() throws FileNotFoundException, IOException, DataFormatException;
	public void save(Analysis analysis) throws IOException, DataFormatException;

}

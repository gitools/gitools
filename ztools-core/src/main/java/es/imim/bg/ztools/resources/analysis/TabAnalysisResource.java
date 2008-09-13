package es.imim.bg.ztools.resources.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.resources.DescriptionFile;
import es.imim.bg.ztools.resources.ResultsFile;
import es.imim.bg.ztools.test.Test;

public class TabAnalysisResource implements AnalysisResource {

	protected String workdir;
	protected boolean resultsOrderByCond;
	protected char separator;
	protected char quote;
	
	public TabAnalysisResource(String workdir, boolean resultsOrderByCond, char separator, char quote) {
		this.workdir = workdir;
		this.resultsOrderByCond = resultsOrderByCond;
		this.separator = separator;
		this.quote = quote;
	}
	
	@Override
	public void save(Analysis analysis) throws IOException {
		
		String dirName = workdir + File.separator + analysis.getName();
		File workDirFile = new File(dirName);
		if (!workDirFile.exists())
			workDirFile.mkdirs();
		
		Test method = analysis.getTestFactory().create(); //FIXME
		
		saveDescription(workDirFile, analysis, method);
		
		saveResults(workDirFile, analysis);
	}

	protected void saveDescription(
			File workDirFile, 
			Analysis analysis,
			Test method) throws IOException {
		
		Writer writer = new FileWriter(new File(
						workDirFile, 
						analysisFileName(analysis.getName())));
		
		DescriptionFile file = new DescriptionFile(separator, quote);
		
		file.setAnalysisName(analysis.getName());
		file.setMethodName(method.getName());
		file.setResultNames(analysis.getResults().getParamNames());
		file.setStartTime(analysis.getStartTime());
		file.setElapsedTime(analysis.getElapsedTime());
		file.setUserName(System.getProperty("user.name"));
		
		file.write(writer);
	}
	
	protected void saveResults(
			File workDirFile, 
			Analysis analysis) throws IOException {
		
		/*Writer writer = new FileWriter(new File(
						workDirFile, 
						resultsFileName(analysis.getName())));*/
		
		File dest = new File(
				workDirFile, 
				resultsFileName(analysis.getName()));
		
		ResultsFile resFile = new ResultsFile(dest);
		resFile.write(analysis.getResults(), resultsOrderByCond);
	}

	protected String analysisFileName(String analysisName) {
		return "analysis.csv";
	}
	
	protected String resultsFileName(String analysisName) {
		return "results.csv";
	}

}

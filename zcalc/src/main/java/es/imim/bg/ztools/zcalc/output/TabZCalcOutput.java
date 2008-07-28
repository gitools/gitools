package es.imim.bg.ztools.zcalc.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import es.imim.bg.ztools.zcalc.analysis.ZCalcAnalysis;
import es.imim.bg.ztools.zcalc.io.ZCalcAnalysisFile;
import es.imim.bg.ztools.zcalc.io.ZCalcResultsFile;
import es.imim.bg.ztools.zcalc.method.ZCalcMethod;

public class TabZCalcOutput implements ZCalcOutput {

	protected String workdir;
	protected char separator;
	protected char quote;
	
	public TabZCalcOutput(String workdir, char separator, char quote) {
		this.workdir = workdir;
		this.separator = separator;
		this.quote = quote;
	}
	
	@Override
	public void save(ZCalcAnalysis analysis) throws IOException {
		
		String dirName = workdir + File.separator + analysis.getName();
		File workDirFile = new File(dirName);
		if (!workDirFile.exists())
			workDirFile.mkdirs();
		
		ZCalcMethod method = analysis.getMethodFactory().create();
		
		saveAnalysis(workDirFile, analysis, method);
		
		saveResults(workDirFile, analysis);
	}

	protected void saveAnalysis(
			File workDirFile, 
			ZCalcAnalysis analysis,
			ZCalcMethod method) throws IOException {
		
		Writer writer = new FileWriter(new File(
						workDirFile, 
						analysisFileName(analysis.getName())));
		
		ZCalcAnalysisFile file = new ZCalcAnalysisFile(separator, quote);
		
		file.setAnalysisName(analysis.getName());
		file.setMethodName(method.getName());
		file.setResultNames(analysis.getResultNames());
		file.setStartTime(analysis.getStartTime());
		file.setElapsedTime(analysis.getElapsedTime());
		file.setUserName(System.getProperty("user.name"));
		
		file.write(writer);
	}
	
	protected void saveResults(
			File workDirFile, 
			ZCalcAnalysis analysis) throws IOException {
		
		Writer writer = new FileWriter(new File(
						workDirFile, 
						resultsFileName(analysis.getName())));
		
		ZCalcResultsFile file = new ZCalcResultsFile(separator, quote);
		
		file.setPropNames(analysis.getPropNames());
		file.setGroupNames(analysis.getGroupNames());
		file.setResultNames(analysis.getResultNames());
		file.setResults(analysis.getResults());
		
		file.write(writer);
	}

	protected String analysisFileName(String analysisName) {
		return "analysis.csv";
	}
	
	protected String resultsFileName(String analysisName) {
		return "results.csv";
	}

}

package es.imim.bg.ztools.zcalc.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.ztools.zcalc.analysis.ZCalcAnalysis;
import es.imim.bg.ztools.zcalc.results.AbstractZCalcResult;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;
import es.imim.bg.ztools.zcalc.test.ZCalcTest;

//TODO actualizar saveParams usando TabWriter
public class CsvZCalcOutput extends TabZCalcOutput {

	public CsvZCalcOutput(String workdir, char separator, char quote) {
		super(workdir, true, separator, quote);
	}

	public void save(ZCalcAnalysis analysis) throws IOException {
		
		File workDirFile = new File(workdir);
		if (!workDirFile.exists())
			workDirFile.mkdirs();
		
		ZCalcTest method = analysis.getMethodFactory().create();
		
		saveAnalysis(workDirFile, analysis, method);
		
		//saveResults(workDirFile, analysis, method);
		
		saveParams(
				analysis.getCondNames(), 
				analysis.getGroupNames(), 
				analysis.getResults(), 
				analysis.getResultNames(),
				workDirFile);
	}
	
	@Deprecated
	private void saveParams(
			String[] propNames, String[] groupNames,
			ObjectMatrix2D results, String[] params, File workDirFile)
			throws FileNotFoundException {
		
		for (int i = 0; i < params.length; i++) {
			final String paramName = params[i];
			final int paramIndex = i;
			
			String fileName = resultFileName(paramName);
			File file = new File(workDirFile, fileName);
			PrintWriter out = new PrintWriter(file);
			
			saveParam(paramName, paramIndex, propNames, groupNames, results, out);
			
			out.close();
		}
	}

	@Deprecated
	private void saveParam(
			String paramName,
			int paramIndex,
			String[] propNames,
			String[] groupNames, 
			ObjectMatrix2D results,
			PrintWriter out) {
		
		printQuoted(out, paramName);
		for (String propName : propNames) {
			out.print(separator);
			printQuoted(out, propName);
		}
		
		out.println();
		
		int numProperties = results.columns();
		int numGroups = results.rows();
		
		for (int groupIndex = 0; groupIndex < numGroups; groupIndex++) {
			
			final String groupName = groupNames[groupIndex];
			
			printQuoted(out, groupName);
			
			for (int propIndex = 0; propIndex < numProperties; propIndex++) {
				
				AbstractZCalcResult cell = 
					(AbstractZCalcResult) results.getQuick(groupIndex, propIndex);
				
				Object[] values = cell.getValues();
				
				Object value = values[paramIndex];
				out.print(separator);
				out.print(value);
			}
			
			out.println();
		}
	}
	
	private String resultFileName(String paramName) {
		return paramName + ".result.csv";
	}
	
	protected void printQuoted(PrintWriter out, String value) {
		out.print(quote);
		out.print(value);
		out.print(quote);
	}
	
	protected void printList(PrintWriter out, String name, String value) {
		out.print(name);
		out.print(separator);
		printQuoted(out, value);
		out.println();
	}

	protected void printList(PrintWriter out, String name, String[] values) {
		out.print(name);
		if (values.length > 0) {
			for (String value : values) {
				out.print(separator);
				printQuoted(out, value);
			}
		}
		out.println();
	}
	
	/*protected void printList(PrintWriter out, String name, List<String> values) {
		out.print(name);
		if (values.size() > 0) {
			for (String value : values) {
				out.print(separator);
				printQuoted(out, value);
			}
		}
		out.println();
	}*/
}

package es.imim.bg.ztools.zcalc.report;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import cern.colt.matrix.ObjectMatrix2D;
import es.imim.bg.ztools.zcalc.io.ZCalcAnalysisFile;
import es.imim.bg.ztools.zcalc.io.ZCalcResultsFile;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class ResultsModelLoader {

	private static final char defaultSep = '\t';
	private static final char defaultQuote = '"';

	public Map<String, Object> load(String source) throws IOException, DataFormatException {
	
		Map<String, Object> model = new HashMap<String, Object>();
		
		Reader analysisReader = null;
		Reader resultsReader = null;
		
		if (source.endsWith(".zip")) {
			//TODO
		}
		else {
			String basePath = source + File.separator;
			analysisReader = new FileReader(new File(basePath + "analysis.csv"));
			resultsReader = new FileReader(new File(basePath + "results.csv"));
		}
		
		loadAnalysis(analysisReader, model);
		loadResults(resultsReader, model);
		
		return model;
	}
	
	protected void loadAnalysis(Reader reader, Map<String, Object> model) throws IOException {
		ZCalcAnalysisFile file = new ZCalcAnalysisFile(defaultSep, defaultQuote);
		
		file.read(reader);
		
		model.put("analysisName", file.getAnalysisName());
		model.put("methodName", file.getMethodName());
		model.put("resultNames", file.getResultNames());
	}

	private void loadResults(Reader reader, Map<String, Object> model) throws IOException, DataFormatException {
		ZCalcResultsFile file = new ZCalcResultsFile(defaultSep, defaultQuote);
		
		file.read(reader);
		
		model.put("condNames", file.getPropNames());
		model.put("groupNames", file.getGroupNames());
		
		final ObjectMatrix2D results = file.getResults();
		model.put("results", new TemplateMethodModel() {
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() < 3)
					throw new TemplateModelException("Wrong arguments, required (groupIndex, condIndex, resultIndex)");
				
				int groupIndex = Integer.parseInt(args.get(0).toString());
				int condIndex = Integer.parseInt(args.get(1).toString());
				int resultIndex = Integer.parseInt(args.get(2).toString());
				double[] values = (double[]) results.get(groupIndex, condIndex);
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				if (values != null) {
					if (resultIndex >= values.length)
						throw new TemplateModelException("result index out of bounds: " + resultIndex);

					map.put("value", String.valueOf(values[resultIndex]));
					map.put("color", "rgb(100%,100%,100%)");
					map.put("alt", String.valueOf(values[resultIndex]));
				}
				else {
					System.err.println("WARN: result value at (" 
							+ groupIndex + "," + condIndex + "," + resultIndex + ") is null.");
					
					map.put("value", "null");
					map.put("color", "rgb(100%,0%,0%)");
					map.put("alt", "(" + groupIndex + "," + condIndex + "," + resultIndex + ")");
				}
				
				return new SimpleHash(map);
			}
		});
	}
}

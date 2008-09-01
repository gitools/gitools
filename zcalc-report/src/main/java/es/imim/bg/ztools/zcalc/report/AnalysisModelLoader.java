package es.imim.bg.ztools.zcalc.report;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import cern.colt.matrix.ObjectMatrix2D;
import es.imim.bg.colorscale.QValueColorScale;
import es.imim.bg.colorscale.util.ColorUtils;
import es.imim.bg.ztools.resources.ResultsFile;
import es.imim.bg.ztools.zcalc.io.ZCalcAnalysisFile;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class AnalysisModelLoader {

	private static final char defaultSep = '\t';
	private static final char defaultQuote = '"';
	
	private static final String analysisResourceName = "analysis.csv";
	private static final String resultsResourceName = "results.csv";

	public Map<String, Object> load(String source) throws IOException, DataFormatException {
	
		Map<String, Object> model = new HashMap<String, Object>();
		
		Reader analysisReader = getResourceReader(source, analysisResourceName);
		Reader resultsReader = getResourceReader(source, resultsResourceName);
		
		loadAnalysis(analysisReader, model);
		loadResults(resultsReader, model);
		
		return model;
	}
	
	public Map<String, Object> loadDescription(String source) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		Reader reader = getResourceReader(source, analysisResourceName);
		loadAnalysis(reader, model);
		return model;
	}
	
	public Map<String, Object> loadResults(String source) throws IOException, DataFormatException {
		Map<String, Object> model = new HashMap<String, Object>();
		Reader reader = getResourceReader(source, resultsResourceName);
		loadResults(reader, model);
		return model;
	}
	
	private Reader getResourceReader(String source, String resource) throws FileNotFoundException {
		Reader reader = null;
		if (source.endsWith(".zip")) {
			//TODO
		}
		else {
			String basePath = source + File.separator;
			reader = new FileReader(new File(basePath + resource));
		}
		return reader;
	}

	protected void loadAnalysis(Reader reader, Map<String, Object> model) throws IOException {
		ZCalcAnalysisFile file = new ZCalcAnalysisFile(defaultSep, defaultQuote);
		
		file.read(reader);
		
		model.put("analysisName", file.getAnalysisName());
		model.put("methodName", file.getMethodName());
		model.put("resultNames", file.getResultNames());
		model.put("userName", file.getUserName());
		model.put("startTime", file.getStartTime());
		model.put("elapsedTime", file.getElapsedTime());
	}

	private void loadResults(Reader reader, Map<String, Object> model) throws IOException, DataFormatException {
		ResultsFile file = new ResultsFile(defaultSep, defaultQuote);
		
		file.read(reader);
		
		model.put("condNames", file.getPropNames());
		model.put("groupNames", file.getGroupNames());
		
		final Map<String, Integer> paramNameIndexMap = new HashMap<String, Integer>();
		int i = 0;
		for (String paramName : file.getResultNames())
			paramNameIndexMap.put(paramName, i++);
		
		final ObjectMatrix2D results = file.getResults();
		
		model.put("paramValue", new TemplateMethodModel() {
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() < 3)
					throw new TemplateModelException("Wrong arguments, required: groupIndex, condIndex, paramName");
				
				int groupIndex = Integer.parseInt(args.get(0).toString());
				int condIndex = Integer.parseInt(args.get(1).toString());
				
				String paramName = args.get(2).toString();
				Integer resultIndex = paramNameIndexMap.get(paramName);
				if (resultIndex == null)
					throw new TemplateModelException("Parameter '" + paramName + "' doesn't exists.");
				
				double[] values = (double[]) results.get(groupIndex, condIndex);
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				Object value = null;
				
				if (values != null) {
					/*if (resultIndex >= values.length)
						throw new TemplateModelException("result index out of bounds: " + resultIndex);*/

					value = new SimpleNumber(values[resultIndex]);
					
					/*map.put("value", String.valueOf(values[resultIndex]));
					map.put("color", "rgb(100%,100%,100%)");
					map.put("alt", String.valueOf(values[resultIndex]));*/
				}
				else {
					System.err.println("WARN: result value at (" 
							+ groupIndex + "," + condIndex + "," + resultIndex + ") is null.");
					
					value = new SimpleNumber(0.0);
					/*map.put("value", "null");
					map.put("color", "rgb(100%,0%,0%)");
					map.put("alt", "(" + groupIndex + "," + condIndex + "," + resultIndex + ")");*/
				}
				
				return value;
				//return new SimpleHash(map);
			}
		});
		
		model.put("colorScale", new TemplateMethodModel() {
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() < 2)
					throw new TemplateModelException("Wrong arguments, required: value, scaleName, [scaleParams]");
				
				//System.out.println(args.get(0).toString());
				Double value = null;
				try {
					value = Double.parseDouble(args.get(0).toString());
				}
				catch (Exception e) {
					return new SimpleScalar("rgb(0,0,0)"); //FIXME
				}
				
				String scaleName = args.get(1).toString();
				
				Color color = Color.BLACK;
				
				if (scaleName.equals("log"))
					color = new QValueColorScale().getColor(value);
				
				String htmlColor = ColorUtils.colorToHtml(color);

				return new SimpleScalar(htmlColor);
			}
		});
	}
}

package es.imim.bg.ztools.resources.analysis;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.test.BinomialTest;
import es.imim.bg.ztools.test.FisherTest;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.ZscoreTest;
import es.imim.bg.ztools.test.ZscoreWithSamplingTest;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.results.BinomialResult;
import es.imim.bg.ztools.utils.Util;

public class REXmlAnalysisResource implements AnalysisResource {

	protected String workdir;
	protected int minModuleSize;
	protected int maxModuleSize;
	
	public REXmlAnalysisResource(String workdir, int minGroupSize, int maxGroupSize) {
		this.workdir = workdir;
		this.minModuleSize = minGroupSize;
		this.maxModuleSize = maxGroupSize;
	}
	
	public void save(Analysis analysis) throws IOException, DataFormatException {
		
		String dirName = workdir + File.separator + analysis.getName();
		File workDirFile = new File(dirName);
		if (!workDirFile.exists())
			workDirFile.mkdirs();
		
		File analysisFile = new File(workDirFile, analysis.getName() + ".xml");
		PrintWriter out = new PrintWriter(analysisFile);
		
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<analysis name=\"" + analysis.getName() + "\" binomial=\"false\">");
		
		String[] condNames = analysis.getResults().getColNames();
		String[] moduleNames = analysis.getResults().getRowNames();
		
		TestFactory testFactory = analysis.getTestFactory();
		Test method = testFactory.create(); //FIXME
		String statName = method.getName();
		
		Results results = analysis.getResults();
		
		final String[] paramNames = results.getParamNames();
		final Map<String, Integer> paramIndexMap = new HashMap<String, Integer>();
		for (int i = 0; i < paramNames.length; i++)
			paramIndexMap.put(paramNames[i], new Integer(i));
		
		int indexOfN = indexOfParam(paramIndexMap, "N");
		int indexOfRightPvalue = indexOfParam(paramIndexMap, "right-p-value");
		
		int numConditions = condNames.length;
		int numModules = moduleNames.length;
		
		out.println("\t<property-list>");
		for (int condIndex = 0; condIndex < numConditions; condIndex++) {
			final String condName = condNames[condIndex];
			out.println("\t\t<name>" + condName + "</name>");
		}
		out.println("\t</property-list>");
		
		final int[][] moduleItemIndices = analysis.getModules().getItemIndices();
		
		for (int moduleIndex = 0; moduleIndex < numModules; moduleIndex++) {
			final String moduleName = moduleNames[moduleIndex];
			
			int greaterN = 0;
			for (int condIndex = 0; condIndex < numConditions; condIndex++)
				greaterN = Math.max(greaterN, 
						(int)results.getDataValue(condIndex, moduleIndex, indexOfN));
		
			int moduleSize = moduleItemIndices[moduleIndex].length;
			
			if (greaterN >= minModuleSize && greaterN <= maxModuleSize) {
				out.println("\t<class name=\"" + moduleName + "\" size=\"" + moduleSize + "\">");
				
				for (int propIndex = 0; propIndex < numConditions; propIndex++) {
					
					final int N = (int)results.getDataValue(propIndex, moduleIndex, indexOfN);
					final double rightPvalue = results.getDataValue(propIndex, moduleIndex, indexOfRightPvalue);
					final double twoTailPvalue = results.getDataValue(
							propIndex, moduleIndex, indexOfParam(paramIndexMap, "two-tail-p-value"));
					
					final String propName = condNames[propIndex];
					
					if (N >= minModuleSize && N <= maxModuleSize) {
						String valueSt = null;
						String zscoreSt = null;
						String pvalueSt = null;
						String miSt = null;
						String sigmaSt = null;
						
						if (method instanceof FisherTest) {
							
							int a = (int) results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "a"));
							int b = (int) results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "b"));
							int c = (int) results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "c"));
							int d = (int) results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "d"));
							
							final double expected = ((double)((a + b) * (a + c)) 
									/ (double)(a + b + c + d));
							
							valueSt = Integer.toString(a);
							zscoreSt = Double.toString(Util.pvalue2rightzscore(rightPvalue));
							pvalueSt = Double.toString(rightPvalue);
							miSt = Double.toString(expected);
							sigmaSt = "[" + a + ", " + b + ", " + c + ", " + d + "]";
						}
						else if (method instanceof ZscoreTest) {
							
							double observed = results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "observed"));
							double expectedMean = results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "expected-mean"));
							double expectedStdev = results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "expected-stdev"));
							double zscore = results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "z-score"));
							
							valueSt = Double.toString(observed);
							zscoreSt = Double.toString(zscore);
							
							if (method instanceof ZscoreWithSamplingTest)
								pvalueSt = Double.toString(twoTailPvalue);
							else
								pvalueSt = Double.toString(rightPvalue);
							
							miSt = Double.toString(expectedMean);
							sigmaSt = Double.toString(expectedStdev);
						}
						else if (method instanceof BinomialTest) {
							double observed = results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "observed"));
							double expectedMean = results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "expected-mean"));
							double expectedStdev = results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "expected-stdev"));
							int aprox = (int) results.getDataValue(
									propIndex, moduleIndex, indexOfParam(paramIndexMap, "aproximation"));
							BinomialResult.AproximationUsed apr = 
								BinomialResult.AproximationUsed.values()[aprox];
							
							valueSt = Double.toString(observed);
							zscoreSt = Double.toString(Util.pvalue2rightzscore(rightPvalue));
							pvalueSt = Double.toString(rightPvalue);
							miSt = Double.toString(expectedMean);
							sigmaSt = Double.toString(expectedStdev) + " aprox:" + apr.toString();
						}
						else
							throw new DataFormatException("Result type not supported by REXmlZCalcOutput.");
	
						out.println("\t\t<property name=\"" + propName + "\" size=\"" + N + "\">");
						out.println("\t\t\t<statistics name=\"" + statName + "\">");
			            
						out.println("\t\t\t\t<value>" + valueSt + "</value>");
						out.println("\t\t\t\t<z-score>" + zscoreSt + "</z-score>");
						out.println("\t\t\t\t<p-value>" + pvalueSt + "</p-value>");
						out.println("\t\t\t\t<distr>");
						out.println("\t\t\t\t\t<mi>" + miSt + "</mi>");
						out.println("\t\t\t\t\t<sigma>" + sigmaSt + "</sigma>");
						out.println("\t\t\t\t</distr>");
		
						out.println("\t\t\t</statistics>");
						out.println("\t\t</property>");
					}
					/*else
						if (cell == null)
							System.out.println("cell is null");
						else
							System.out.println("cell n = " + cell.getN());*/
				}
				
				//TODO order by sum tag
				
				out.println("\t</class>");
			}
		}
		
		out.println("\t<stat-list>\n\t\t<name>" + statName + "</name>\n\t</stat-list>");
		out.println("\t<genes>\n\t</genes>");
		out.print("</analysis>");
		
		out.close();
	}

	private int indexOfParam(Map<String, Integer> paramIndexMap, String name) throws DataFormatException {
		int index = 0;
		try {
			index = paramIndexMap.get(name);
		}
		catch (Exception e) {
			throw new DataFormatException("Parameter called '" + name + "' expected in results.");
		}
		return index;
	}

	@Override
	public Analysis load() {
		throw new UnsupportedOperationException("RE xml loading unsupported.");
	}
}

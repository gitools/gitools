package org.gitools.persistence.analysis;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.gitools.model.Analysis;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.AnalysisPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.stats.test.BinomialTest;
import org.gitools.stats.test.FisherTest;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.ZscoreTest;
import org.gitools.stats.test.ZscoreWithSamplingTest;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.BinomialResult.Distribution;
import org.gitools.utils.Util;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class REXmlAnalysisResource extends AnalysisPersistence {

	private static final long serialVersionUID = 6487157855012145649L;

	protected int minModuleSize;
	protected int maxModuleSize;
	
	public REXmlAnalysisResource(String basePath, int minGroupSize, int maxGroupSize) {
		super(basePath);
		this.minModuleSize = minGroupSize;
		this.maxModuleSize = maxGroupSize;
	}
	
	@Override
	public void save(
			Analysis analysis, 
			IProgressMonitor monitor)
			throws PersistenceException {
		
		monitor.begin("Saving analysis in rexml format...", 1);
		
		try {
			String dirName = basePath /*+ File.separator + analysis.getName()*/;
			File workDirFile = new File(dirName);
			if (!workDirFile.exists())
				workDirFile.mkdirs();
			
			File analysisFile = new File(workDirFile, analysis.getTitle() + ".xml");
			PrintWriter out = new PrintWriter(analysisFile);
			
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<analysis name=\"" + analysis.getTitle() + "\" binomial=\"false\">");
			
			ObjectMatrix resultsMatrix = analysis.getResultsMatrix();
			
			String[] condNames = new String[resultsMatrix.getColumnCount()];
			for (int i = 0; i < condNames.length; i++)
				condNames[i] = resultsMatrix.getColumn(i).toString();
			
			String[] moduleNames = new String[resultsMatrix.getRowCount()];
			for (int i = 0; i < moduleNames.length; i++)
				moduleNames[i] = resultsMatrix.getRow(i).toString();
			
			TestFactory testFactory = 
				TestFactory.createFactory(analysis.getToolConfig());
			
			Test test = testFactory.create(); //FIXME?
			String statName = test.getName();
			
			List<IElementAttribute> properties = resultsMatrix.getCellAdapter().getProperties();
	
			final String[] paramNames = new String[properties.size()];
			final Map<String, Integer> paramIndexMap = new HashMap<String, Integer>();
			for (int i = 0; i < properties.size(); i++) {
				final IElementAttribute prop = properties.get(i);
				paramNames[i] = prop.getId();
				paramIndexMap.put(paramNames[i], i);
			}
			
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
			
			final int[][] moduleItemIndices = analysis.getModuleMap().getItemIndices();
			
			for (int moduleIndex = 0; moduleIndex < numModules; moduleIndex++) {
				final String moduleName = moduleNames[moduleIndex];
				
				int greaterN = 0;
				for (int condIndex = 0; condIndex < numConditions; condIndex++)
					greaterN = Math.max(greaterN, 
							(Integer) resultsMatrix.getCellValue(moduleIndex, condIndex, indexOfN));
			
				int moduleSize = moduleItemIndices[moduleIndex].length;
				
				if (greaterN >= minModuleSize && greaterN <= maxModuleSize) {
					out.println("\t<class name=\"" + moduleName + "\" size=\"" + moduleSize + "\">");
	
					String sumLabel = "mean";
					double sum = 0;
					
					for (int propIndex = 0; propIndex < numConditions; propIndex++) {
						
						final int N = (Integer) resultsMatrix.getCellValue(
								moduleIndex, propIndex, indexOfN);
						final double rightPvalue = (Double) resultsMatrix.getCellValue(
								moduleIndex, propIndex, indexOfRightPvalue);
						final double twoTailPvalue = (Double) resultsMatrix.getCellValue(
								moduleIndex, propIndex, "two-tail-p-value");
						
						final String propName = condNames[propIndex];
						
						if (N >= minModuleSize && N <= maxModuleSize) {
							
							sum += rightPvalue;
							
							String valueSt = null;
							String zscoreSt = null;
							String pvalueSt = null;
							String miSt = null;
							String sigmaSt = null;
							
							if (test instanceof FisherTest) {
								
								int a = ((Double)resultsMatrix.getCellValue(
										moduleIndex, propIndex, "a")).intValue();
								int b = ((Double)resultsMatrix.getCellValue(
										moduleIndex, propIndex, "b")).intValue();
								int c = ((Double)resultsMatrix.getCellValue(
										moduleIndex, propIndex, "c")).intValue();
								int d = ((Double)resultsMatrix.getCellValue(
										moduleIndex, propIndex, "d")).intValue();
								
								final double expected = ((double)((a + b) * (a + c)) 
										/ (double)(a + b + c + d));
								
								valueSt = Integer.toString(a);
								zscoreSt = Double.toString(Util.pvalue2rightzscore(rightPvalue));
								pvalueSt = Double.toString(rightPvalue);
								miSt = Double.toString(expected);
								sigmaSt = "[" + a + ", " + b + ", " + c + ", " + d + "]";
							}
							else if (test instanceof ZscoreTest) {
								
								double observed = (Double) resultsMatrix.getCellValue(
										moduleIndex, propIndex, "observed");
								double expectedMean = (Double) resultsMatrix.getCellValue(
										moduleIndex, propIndex, "expected-mean");
								double expectedStdev = (Double) resultsMatrix.getCellValue(
										moduleIndex, propIndex, "expected-stdev");
								double zscore = (Double) resultsMatrix.getCellValue(
										moduleIndex, propIndex, "z-score");
								
								valueSt = Double.toString(observed);
								zscoreSt = Double.toString(zscore);
								
								if (test instanceof ZscoreWithSamplingTest)
									pvalueSt = Double.toString(twoTailPvalue);
								else
									pvalueSt = Double.toString(rightPvalue);
								
								miSt = Double.toString(expectedMean);
								sigmaSt = Double.toString(expectedStdev);
							}
							else if (test instanceof BinomialTest) {
								double observed = (Integer) resultsMatrix.getCellValue(
										moduleIndex, propIndex, "observed");
								double expectedMean = (Double) resultsMatrix.getCellValue(
										moduleIndex, propIndex, "expected-mean");
								double expectedStdev = (Double) resultsMatrix.getCellValue(
										moduleIndex, propIndex, "expected-stdev");
								BinomialResult.Distribution apr =
									(Distribution) resultsMatrix.getCellValue(
									moduleIndex, propIndex, "aproximation");
	
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
					
					out.println("\t\t<order>");
					out.println("\t\t\t<statistics name=\"" + sumLabel + "\" value=\"" + sum + "\"/>");
					out.println("\t\t</order>");
					
					out.println("\t</class>");
				}
			}
			
			out.println("\t<stat-list>\n\t\t<name>" + statName + "</name>\n\t</stat-list>");
			out.println("\t<genes>\n\t</genes>");
			out.print("</analysis>");
			
			out.close();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		
		monitor.end();
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
	public Analysis load(IProgressMonitor monitor) {
		throw new UnsupportedOperationException("RE xml loading unsupported.");
	}
}

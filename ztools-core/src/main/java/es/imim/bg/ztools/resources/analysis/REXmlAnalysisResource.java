package es.imim.bg.ztools.resources.analysis;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.DataFormatException;

import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.ZscoreWithSamplingTest;
import es.imim.bg.ztools.test.results.BinomialResult;
import es.imim.bg.ztools.test.results.CommonResult;
import es.imim.bg.ztools.test.results.FisherResult;
import es.imim.bg.ztools.test.results.ZScoreResult;
import es.imim.bg.ztools.utils.Util;

public class REXmlAnalysisResource implements AnalysisResource {

	protected String workdir;
	protected int minGroupSize;
	protected int maxGroupSize;
	
	public REXmlAnalysisResource(String workdir, int minGroupSize, int maxGroupSize) {
		this.workdir = workdir;
		this.minGroupSize = minGroupSize;
		this.maxGroupSize = maxGroupSize;
	}
	
	public void save(Analysis analysis) throws IOException, DataFormatException {
		
		File workDirFile = new File(workdir);
		if (!workDirFile.exists())
			workDirFile.mkdirs();
		
		File analysisFile = new File(workDirFile, analysis.getName() + ".xml");
		PrintWriter out = new PrintWriter(analysisFile);
		
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<analysis name=\"" + analysis.getName() + "\" binomial=\"false\">");
		
		String[] propNames = analysis.getCondNames();
		String[] groupNames = analysis.getGroupNames();
		int[][] groupItemIndices = analysis.getGroupItemIndices();
		
		Test method = analysis.getTestFactory().create(); //FIXME
		String statName = method.getName();
		
		ObjectMatrix2D results = analysis.getResults();
		
		int numProperties = propNames.length;
		int numGroups = groupNames.length;
		
		out.println("\t<property-list>");
		for (int propIndex = 0; propIndex < numProperties; propIndex++) {
			final String propName = propNames[propIndex];
			out.println("\t\t<name>" + propName + "</name>");
		}
		out.println("\t</property-list>");
		
		for (int groupIndex = 0; groupIndex < numGroups; groupIndex++) {
			final String groupName = groupNames[groupIndex];
			final int[] groupItems = groupItemIndices[groupIndex];
			final int groupSize = groupItems.length;
			
			if (groupSize >= minGroupSize) {
				out.println("\t<class name=\"" + groupName + "\" size=\"" + groupSize + "\">");
				
				for (int propIndex = 0; propIndex < numProperties; propIndex++) {
					
					//if (propGroupSize >= minGroupSize) {
					final String propName = propNames[propIndex];
					CommonResult cell = 
						(CommonResult) results.getQuick(groupIndex, propIndex);
				
					//if (cell != null && cell.getN() >= minGroupSize && cell.getN() <= maxGroupSize) {
						String valueSt = null;
						String zscoreSt = null;
						String pvalueSt = null;
						String miSt = null;
						String sigmaSt = null;
						
						if (cell instanceof FisherResult) {
							FisherResult fcell = (FisherResult) cell;
							
							final double expected = ((double)((fcell.a + fcell.b) * (fcell.a + fcell.c)) 
									/ (double)(fcell.a + fcell.b + fcell.c + fcell.d));
							
							valueSt = Integer.toString(fcell.a);
							zscoreSt = Double.toString(Util.pvalue2zscore(fcell.rightPvalue));
							pvalueSt = Double.toString(fcell.rightPvalue);
							miSt = Double.toString(expected);
							sigmaSt = "[" + fcell.a + ", " + fcell. b + ", " + fcell.c + ", " + fcell.d + "]";
						}
						else if (cell instanceof ZScoreResult) {
							ZScoreResult zcell = (ZScoreResult) cell;
							
							valueSt = Double.toString(zcell.observed);
							zscoreSt = Double.toString(zcell.zscore);
							
							if (method instanceof ZscoreWithSamplingTest)
								pvalueSt = Double.toString(zcell.twoTailPvalue);
							else
								pvalueSt = Double.toString(zcell.rightPvalue);
							
							miSt = Double.toString(zcell.expectedMean);
							sigmaSt = Double.toString(zcell.expectedStdev);
						}
						else if (cell instanceof BinomialResult) {
							BinomialResult pcell = (BinomialResult) cell;
							
							valueSt = Double.toString(pcell.observed);
							zscoreSt = Double.toString(Util.pvalue2zscore(pcell.rightPvalue));
							pvalueSt = Double.toString(pcell.rightPvalue);
							miSt = Double.toString(pcell.expectedMean);
							sigmaSt = Double.toString(pcell.expectedStdev) + " aprox:" + pcell.aprox.toString();
						}
						else
							throw new DataFormatException("Result type not supported by REXmlZCalcOutput.");
	
						out.println("\t\t<property name=\"" + propName + "\" size=\"" + groupSize + "\">");
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
					//}
					/*else
						if (cell == null)
							System.out.println("cell is null");
						else
							System.out.println("cell n = " + cell.getN());*/
				}
				
				out.println("\t</class>");
			}
		}
		
		out.println("\t<stat-list>\n\t\t<name>" + statName + "</name>\n\t</stat-list>");
		out.println("\t<genes>\n\t</genes>");
		out.print("</analysis>");
		
		out.close();
	}
}

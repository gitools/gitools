/*
 *  Copyright 2010 cperez.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.analysis.combination;

import cern.jet.stat.Probability;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Arrays;
import java.util.Date;
import org.gitools.analysis.AnalysisException;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.MatrixViewTransposition;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.model.ModuleMap;


public class CombinationProcessor {

	private CombinationAnalysis analysis;

	public CombinationProcessor(CombinationAnalysis analysis) {
		this.analysis = analysis;
	}
	
	public void run(IProgressMonitor monitor) throws AnalysisException {

		Date startTime = new Date();

		// Prepare data
		IMatrix data = analysis.getData();
		if (analysis.isTransposeData())
			data = new MatrixViewTransposition(data);

		final int numCols = data.getColumnCount();
		final int numRows = data.getRowCount();

		String[] labels = new String[numCols];
		for (int i = 0; i < numCols; i++)
			labels[i] = data.getColumnLabel(i);

		String combOf = analysis.isTransposeData() ? "rows" : "columns";

		// Prepare columns map
		ModuleMap cmap = analysis.getGroupsMap();
		if (cmap != null)
			cmap = cmap.remap(labels);
		else
			cmap = new ModuleMap("All data " + combOf, labels);
		
		analysis.setGroupsMap(cmap);

		// Prepare results matrix
		final ObjectMatrix results = new ObjectMatrix();

		String[] cclabels = cmap.getModuleNames();
		cclabels = Arrays.copyOf(cclabels, cclabels.length);
		String[] rlabels = new String[numRows];
		for (int i = 0; i < numRows; i++)
			rlabels[i] = data.getRowLabel(i);

		results.setColumns(cclabels);
		results.setRows(rlabels);
		results.makeCells();

		results.setCellAdapter(
				new BeanElementAdapter(CombinationResults.class));

		analysis.setResults(results);

		// Run combination
		int sizeIndex = -1;
		String sizeAttrName = analysis.getSizeAttrName();
		/*if (sizeAttrName == null || sizeAttrName.isEmpty())
			sizeIndex = analysis.getSizeAttrIndex();*/
		if (sizeAttrName != null && !sizeAttrName.isEmpty())
			sizeIndex = data.getCellAdapter().getPropertyIndex(sizeAttrName);
		
		int pvalueIndex = 0;
		String pvalueAttrName = analysis.getPvalueAttrName();
		/*if (pvalueAttrName == null || pvalueAttrName.isEmpty())
			pvalueIndex = analysis.getPvalueAttrIndex();*/
		if (pvalueAttrName != null && !pvalueAttrName.isEmpty())
			pvalueIndex = data.getCellAdapter().getPropertyIndex(pvalueAttrName);

		MatrixUtils.DoubleCast sizeCast = null;

		if (sizeIndex >= 0)
			sizeCast = MatrixUtils.createDoubleCast(
				data.getCellAdapter().getProperty(sizeIndex).getValueClass());

		MatrixUtils.DoubleCast pvalueCast = MatrixUtils.createDoubleCast(
				data.getCellAdapter().getProperty(pvalueIndex).getValueClass());

		int numCC = cmap.getModuleCount();

		monitor.begin("Running combination analysis ...", numCC * numRows);

		for (int cmi = 0; cmi < numCC; cmi++) {
			int[] cindices = cmap.getItemIndices(cmi);
			for (int ri = 0; ri < numRows; ri++) {
				int n = 0;
				double sumSizeZ = 0;
				double sumSizeSqr = 0;

				for (int ci = 0; ci < cindices.length; ci++) {
					int mci = cindices[ci];
					
					if (data.getCell(ri, mci) != null) {
						double size = sizeIndex < 0 ? 1
								: sizeCast.getDoubleValue(
									data.getCellValue(ri, mci, sizeIndex));

						double pvalue = pvalueCast.getDoubleValue(
								data.getCellValue(ri, mci, pvalueIndex));

						double zscore = pvalueToZscore(pvalue);

						if (!Double.isNaN(size + pvalue + zscore)) {
							n++;
							sumSizeZ += size * zscore;
							sumSizeSqr += size * size;
						}
					}
				}

				double zcomb = sumSizeZ / Math.sqrt(sumSizeSqr);
				double pvalue = zscoreToPvalue(zcomb);

				CombinationResults r = new CombinationResults();
				r.setN(n);
				r.setZscore(zcomb);
				r.setPvalue(pvalue);

				results.setCell(ri, cmi, r);

				monitor.worked(1);
			}
		}

		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

		monitor.end();
	}

	private double pvalueToZscore(double pvalue) {
		if (Double.isNaN(pvalue))
			return pvalue;

		pvalue = 1.0 - pvalue;
		pvalue = pvalue < 0.000001 ? 0.000001 : pvalue;
		pvalue = pvalue > 0.999999 ? 0.999999 : pvalue;

		double zscore = Probability.normalInverse(pvalue);
		return zscore;
	}

	private double zscoreToPvalue(double zscore) {
		if (Double.isNaN(zscore))
			return zscore;

		double pvalue = 1.0 - Probability.normal(zscore);
		return pvalue;
	}

}

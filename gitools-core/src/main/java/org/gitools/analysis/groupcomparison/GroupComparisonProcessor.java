/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.analysis.groupcomparison;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestProcessor;
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.MannWhitneyWilxoxonTest;


public class GroupComparisonProcessor extends HtestProcessor {

	private GroupComparisonAnalysis analysis;

	public GroupComparisonProcessor(GroupComparisonAnalysis analysis) {
		this.analysis = analysis;
	}
	
	@Override
	public void run(IProgressMonitor monitor) throws AnalysisException {

		Date startTime = new Date();

		// Prepare data
		IMatrix data = analysis.getData();
		if (analysis.isTransposeData())
			data = new TransposedMatrixView(data);

		final int numRows = data.getRowCount();


		// Prepare results matrix
		final ObjectMatrix resultsMatrix = new ObjectMatrix();

		String[] columnLabels = new String[1];
		columnLabels[0] = analysis.getTest().getName();
		String[] rlabels = new String[numRows];
		for (int i = 0; i < numRows; i++)
			rlabels[i] = data.getRowLabel(i);

		resultsMatrix.setColumns(columnLabels);
		resultsMatrix.setRows(rlabels);
		resultsMatrix.makeCells();

		resultsMatrix.setCellAdapter(
				new BeanElementAdapter(GroupComparisonResult.class));

		

		// Run group comparison

		monitor.begin("Running group comparison analysis ...", numRows);

		int attrIndex = analysis.getAttributeIndex();

		Class<?> valueClass = data.getCellAttributes().get(attrIndex).getValueClass();
		final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);


		for (int column = 0; column < columnLabels.length; column++) {
			for (int row = 0; row < numRows; row++) {

				int[] group1 = getColumnIndices(data,
												analysis.getGroups1(),
												row);
				int[] group2 = getColumnIndices(data,
												analysis.getGroups2(),
												row);


				double[] groupVals1 = new double[group1.length];
				double[] groupVals2 = new double[group2.length];				
				for (int gi = 0; gi < group1.length; gi++) {
					Object value = data.getCellValue(row, group1[gi], attrIndex);
					Double v = cast.getDoubleValue(value);
					if (v == null || Double.isNaN(v)) 
						v = Double.NaN;
					groupVals1[gi] = v;
				}
				for (int gi = 0; gi < group2.length; gi++) {
					Object value = data.getCellValue(row, group2[gi], attrIndex);
					Double v = cast.getDoubleValue(value);
					if (v == null || Double.isNaN(v))
						v = Double.NaN;
					groupVals2[gi] = v;
				}

				MannWhitneyWilxoxonTest test = (MannWhitneyWilxoxonTest) analysis.getTest();
				GroupComparisonResult r = test.processTest(groupVals1, groupVals2);

				resultsMatrix.setCell(row, column, r);

				monitor.worked(1);
			}
		}

		analysis.setResults(resultsMatrix);
		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

				/* Multiple test correction */

		MTC mtc = analysis.getMtc();

		multipleTestCorrection(
				resultsMatrix,
				mtc,
				monitor.subtask());

		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

		analysis.setResults(resultsMatrix);

		monitor.end();

	}

	private int[] getColumnIndices(IMatrix data, ColumnGroup group, int row) {
		if (group.getColumns() != null && group.getColumns().length > 0)
			return group.getColumns();

		int attrIndex = group.getCutoffAttributeIndex();
		Class<?> valueClass = data.getCellAttributes().get(attrIndex).getValueClass();
		final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);
		//TODO: place the cast in a different place: inside Group??

		BinaryCutoff binaryCutoff = group.getBinaryCutoff();

		List<Integer> columnIndicesList = new ArrayList<Integer>();
		for (int col = 0; col < data.getColumnCount(); col++) {
			Object value = data.getCellValue(row, col, attrIndex);
			Double v = cast.getDoubleValue(value);
			if (v == null || Double.isNaN(v)) {
				continue;
			}
			else  {
				double compliesCutoff = binaryCutoff.apply(v);
				if (compliesCutoff == 1.0)
					columnIndicesList.add(col);
			}
		}

		Integer[] columnIndices = new Integer[columnIndicesList.size()];
		return	ArrayUtils.toPrimitive(columnIndicesList.toArray(columnIndices));
		//return columnIndicesPrimitive;

	}

}

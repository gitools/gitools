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

package org.gitools.analysis.htest;

import cern.colt.function.DoubleProcedure;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.results.CommonResult;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.analysis.AnalysisProcessor;

public abstract class HtestProcessor implements AnalysisProcessor {

	protected static final DoubleProcedure notNaNProc =
		new DoubleProcedure() {
			@Override public boolean apply(double element) {
					return !Double.isNaN(element); } };

	protected void multipleTestCorrection(
			ObjectMatrix res,
			MTC mtc,
			IProgressMonitor monitor) {
		
		monitor.begin(mtc.getName() + " correction...", 1);
		
		DoubleMatrix2D adjpvalues = DoubleFactory2D.dense.make(3, res.getRowCount());
		for (int condIdx = 0; condIdx < res.getColumnCount(); condIdx++) {
			for (int moduleIdx = 0; moduleIdx < res.getRowCount(); moduleIdx++) {
				CommonResult r = (CommonResult) res.getCell(moduleIdx, condIdx);
				adjpvalues.setQuick(0, moduleIdx, r != null ? r.getLeftPvalue() : Double.NaN);
				adjpvalues.setQuick(1, moduleIdx, r != null ? r.getRightPvalue() : Double.NaN);
				adjpvalues.setQuick(2, moduleIdx, r != null ? r.getTwoTailPvalue() : Double.NaN);
			}
			
			mtc.correct(adjpvalues.viewRow(0).viewSelection(notNaNProc));
			mtc.correct(adjpvalues.viewRow(1).viewSelection(notNaNProc));
			mtc.correct(adjpvalues.viewRow(2).viewSelection(notNaNProc));
			
			for (int moduleIdx = 0; moduleIdx < res.getRowCount(); moduleIdx++) {
				CommonResult r = (CommonResult) res.getCell(moduleIdx, condIdx);
				if (r != null) {
					r.setCorrLeftPvalue(adjpvalues.getQuick(0, moduleIdx));
					r.setCorrRightPvalue(adjpvalues.getQuick(1, moduleIdx));
					r.setCorrTwoTailPvalue(adjpvalues.getQuick(2, moduleIdx));
				}
			}
		}
		
		monitor.end();
	}
}

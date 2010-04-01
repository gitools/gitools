package org.gitools.analysis.htest;

import cern.colt.function.DoubleProcedure;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.results.CommonResult;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class HtestProcessor {

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

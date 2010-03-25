package org.gitools.analysis.htest;

import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.results.CommonResult;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class HtestProcessor {

	protected void multipleTestCorrection(
			ObjectMatrix res,
			MTC mtc,
			IProgressMonitor monitor) {
		
		monitor.begin(mtc.getName() + " correction...", 1);
		
		DoubleMatrix2D adjpvalues = DoubleFactory2D.dense.make(3, res.getRowCount());
		for (int condIdx = 0; condIdx < res.getColumnCount(); condIdx++) {
			for (int moduleIdx = 0; moduleIdx < res.getRowCount(); moduleIdx++) {
				CommonResult r = (CommonResult) res.getCell(moduleIdx, condIdx);
				adjpvalues.setQuick(0, moduleIdx, r.getLeftPvalue());
				adjpvalues.setQuick(1, moduleIdx, r.getRightPvalue());
				adjpvalues.setQuick(2, moduleIdx, r.getTwoTailPvalue());
			}
			
			mtc.correct(adjpvalues.viewRow(0));
			mtc.correct(adjpvalues.viewRow(1));
			mtc.correct(adjpvalues.viewRow(2));
			
			for (int moduleIdx = 0; moduleIdx < res.getRowCount(); moduleIdx++) {
				CommonResult r = (CommonResult) res.getCell(moduleIdx, condIdx);
				r.corrLeftPvalue = adjpvalues.getQuick(0, moduleIdx);
				r.corrRightPvalue = adjpvalues.getQuick(1, moduleIdx);
				r.corrTwoTailPvalue = adjpvalues.getQuick(2, moduleIdx);
			}
		}
		
		monitor.end();
	}
}

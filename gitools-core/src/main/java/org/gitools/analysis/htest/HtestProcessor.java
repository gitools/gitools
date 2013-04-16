/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.analysis.htest;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.matrix.model.matrix.ObjectMatrix;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

public abstract class HtestProcessor implements AnalysisProcessor
{

    protected static final DoubleProcedure notNaNProc = new DoubleProcedure()
    {
        @Override
        public boolean apply(double element)
        {
            return !Double.isNaN(element);
        }
    };

    protected void multipleTestCorrection(@NotNull ObjectMatrix res, @NotNull MTC mtc, @NotNull IProgressMonitor monitor)
    {

        monitor.begin(mtc.getName() + " correction...", 1);

        DoubleMatrix2D adjpvalues = DoubleFactory2D.dense.make(3, res.getRows().size());
        for (int condIdx = 0; condIdx < res.getColumns().size(); condIdx++)
        {
            for (int moduleIdx = 0; moduleIdx < res.getRows().size(); moduleIdx++)
            {
                CommonResult r = (CommonResult) res.getObjectCell(moduleIdx, condIdx);
                adjpvalues.setQuick(0, moduleIdx, r != null ? r.getLeftPvalue() : Double.NaN);
                adjpvalues.setQuick(1, moduleIdx, r != null ? r.getRightPvalue() : Double.NaN);
                adjpvalues.setQuick(2, moduleIdx, r != null ? r.getTwoTailPvalue() : Double.NaN);
            }

            mtc.correct(adjpvalues.viewRow(0).viewSelection(notNaNProc));
            mtc.correct(adjpvalues.viewRow(1).viewSelection(notNaNProc));
            mtc.correct(adjpvalues.viewRow(2).viewSelection(notNaNProc));

            for (int moduleIdx = 0; moduleIdx < res.getRows().size(); moduleIdx++)
            {
                CommonResult r = (CommonResult) res.getObjectCell(moduleIdx, condIdx);
                if (r != null)
                {
                    r.setCorrLeftPvalue(adjpvalues.getQuick(0, moduleIdx));
                    r.setCorrRightPvalue(adjpvalues.getQuick(1, moduleIdx));
                    r.setCorrTwoTailPvalue(adjpvalues.getQuick(2, moduleIdx));
                }
            }
        }

        monitor.end();
    }
}

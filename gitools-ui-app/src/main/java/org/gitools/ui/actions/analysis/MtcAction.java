/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.actions.analysis;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.stats.mtc.MTC;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @noinspection ALL
 */
public class MtcAction extends BaseAction {

    private static final long serialVersionUID = 991170566166881702L;

    private final MTC mtc;

    public MtcAction(@NotNull MTC mtc) {
        super(mtc.getName());

        setDesc("Calculate " + mtc.getName() + " multiple test correction");

        this.mtc = mtc;
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = ActionUtils.getMatrixView();

        if (matrixView == null) {
            return;
        }

        //IElementAdapter cellAdapter = matrixView.getCellAdapter();

        final int propIndex = matrixView.getLayers().getTopLayerIndex();
        final int corrPropIndex = MatrixUtils.correctedValueIndex(matrixView.getLayers(), matrixView.getLayers().get(propIndex));

        if (corrPropIndex < 0) {
            JOptionPane.showMessageDialog(AppFrame.get(), "The property selected doesn't allow multiple test correction.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {

                IMatrix contents = matrixView.getContents();

                int rowCount = contents.getRows().size();
                int columnCount = contents.getColumns().size();

                monitor.begin("Multiple test correction for  " + mtc.getName() + " ...", columnCount * 3);

                DoubleMatrix2D values = DoubleFactory2D.dense.make(rowCount, columnCount);

                for (int col = 0; col < columnCount; col++) {
                    for (int row = 0; row < rowCount; row++)
                        values.setQuick(row, col, MatrixUtils.doubleValue(contents.getValue(row, col, propIndex)));

                    monitor.worked(1);
                }

                for (int col = 0; col < columnCount; col++) {
                    DoubleMatrix1D columnValues = values.viewColumn(col).viewSelection(new DoubleProcedure() {
                        @Override
                        public boolean apply(double v) {
                            return !Double.isNaN(v);
                        }
                    });
                    mtc.correct(columnValues);

                    monitor.worked(1);
                }

                for (int col = 0; col < columnCount; col++) {
                    for (int row = 0; row < rowCount; row++)
                        contents.setValue(row, col, corrPropIndex, values.getQuick(row, col));

                    monitor.worked(1);
                }

                monitor.end();
            }
        });

        AppFrame.get().setStatusText(mtc.getName() + " done.");
    }
}

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

import org.gitools.analysis.stats.mtc.MTC;
import org.gitools.analysis.stats.mtc.MTCFactory;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.position.IMatrixFunction;
import org.gitools.api.matrix.position.IMatrixPosition;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.ui.actions.HeatmapAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MtcAction extends HeatmapAction {

    private static final long serialVersionUID = 991170566166881702L;

    private final MTC mtc;

    public MtcAction(MTC mtc) {
        super(mtc.getName());
        setDesc("Calculate " + mtc.getName() + " multiple test correction");
        this.mtc = mtc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = getHeatmap();
        final IMatrixLayer<Double> valueLayer = matrixView.getLayers().getTopLayer();
        final IMatrixLayer<Double> correctedLayer = correctedValueIndex(matrixView.getLayers(), valueLayer);

        if (correctedLayer == null) {
            JOptionPane.showMessageDialog(Application.get(), "The property selected doesn't allow multiple test correction.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {

                IMatrix contents = matrixView.getContents();
                IMatrixDimension rows = contents.getRows();
                IMatrixDimension columns = contents.getColumns();
                IMatrixFunction<Double, Double> mtcFunction = MTCFactory.createFunction(mtc);

                monitor.begin("Multiple test correction for  " + mtc.getName() + " ...", columns.size() * rows.size() * 3);

                IMatrixPosition position = contents.newPosition();
                for (String column : position.iterate(columns)) {

                    position.iterate(valueLayer, rows)
                            .monitor(monitor)
                            .transform(mtcFunction)
                            .store(contents, correctedLayer);

                }

                monitor.end();
            }
        });

        Application.get().setStatusText(mtc.getName() + " done.");
    }

    private static IMatrixLayer<Double> correctedValueIndex(IMatrixLayers<IMatrixLayer> layers, IMatrixLayer<Double> valueLayer) {
        String id = "corrected-" + valueLayer.getId();
        for (IMatrixLayer correctedLayer : layers)
            if (id.equals(correctedLayer.getId())) {
                return correctedLayer;
            }
        return null;
    }
}

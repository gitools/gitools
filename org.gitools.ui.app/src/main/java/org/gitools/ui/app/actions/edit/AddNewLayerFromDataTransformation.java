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
package org.gitools.ui.app.actions.edit;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class AddNewLayerFromDataTransformation extends HeatmapAction {


    public AddNewLayerFromDataTransformation() {
        super("New data layer...");
        setSmallIconFromResource(IconNames.add16);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return (model instanceof Heatmap) && (((Heatmap) model).getContents() instanceof HashMatrix);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap heatmap = getHeatmap();
        final JobRunnable transformer = new JobRunnable() {

            @Override
            public void run(IProgressMonitor monitor) throws Exception {
                MatrixLayer layer = heatmap.getLayers().getTopLayer();
                IMatrixPosition matrixPointer = heatmap.newPosition();

                MatrixLayer<Double> newLayer = new MatrixLayer<>("log-FC", Double.class, "log", "Calculated logN mean fold change");
                HashMatrix mainData = (HashMatrix) heatmap.getContents();
                mainData.addLayer(newLayer);
                heatmap.getLayers().initLayer(newLayer);

                //monitor.begin("Aggregating values...", clusterDimension.size());

                ArrayList<TransformFunction> transformFunctions = new ArrayList<>();
                transformFunctions.add(new TransformFunction("logN") {
                    @Override
                    public Double apply(Double value, IMatrixPosition position) {
                        if (value != null) {
                            return Math.log(value);
                        }
                        return null;
                    }
                });
                /*transformFunctions.add(new TransformFunction("times2") {
                    @Override
                    public Double apply(Double value, IMatrixPosition position) {
                        if (value != null) {
                            return value*2;
                        }
                        return null;
                    }
                });*/
                transformFunctions.add(new MeanFoldChange(heatmap, newLayer, monitor));


                for (TransformFunction transformFunction : transformFunctions) {
                    matrixPointer.iterate(layer)
                            .monitor(monitor, "<html><body>Applying data transformation <b>'" + transformFunction.getName() + "'</b></html></body>")
                            .transform(transformFunction)
                            .store(heatmap, newLayer);
                    if (newLayer != layer) {
                        layer = newLayer;
                    }
                }
                monitor.end();



                heatmap.getLayers().updateLayers();
                heatmap.getLayers().setTopLayer(heatmap.getLayers().get(newLayer.getId()));
                Application.get().showNotification("New data layer added");
                //copyLayerValues(heatmap, layer, );
            }
        };
        JobThread.execute(Application.get(), transformer);

    }

    private static void copyLayerValues(IMatrix fromMatrix, IMatrixLayer fromLayer, IMatrix toMatrix, IMatrixLayer toLayer) {

        if ((fromMatrix instanceof HashMatrix) && (toMatrix instanceof HashMatrix)) {
            HashMatrix fromHM = (HashMatrix) fromMatrix;
            HashMatrix toHM = (HashMatrix) toMatrix;
            toHM.copyLayerValues(fromLayer.getId(), fromHM);
        } else {
            for (String column : fromMatrix.getColumns()) {
                for (String row : fromMatrix.getRows()) {
                    toMatrix.set(toLayer, fromMatrix.get(fromLayer, row, column), row, column);
                }
            }
        }

    }

}

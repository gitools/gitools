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
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.impl.LinearDecorator;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.ui.app.actions.data.transform.TransformWizard;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.aggregation.MaxAggregator;
import org.gitools.utils.aggregation.MedianAggregator;
import org.gitools.utils.aggregation.MinAggregator;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class AddNewLayerFromDataTransformation extends HeatmapAction {


    public AddNewLayerFromDataTransformation() {
        super("New data from transformations...");
        setSmallIconFromResource(IconNames.calc16);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return (model instanceof Heatmap) && (((Heatmap) model).getContents() instanceof HashMatrix);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap heatmap = getHeatmap();

        final TransformWizard wizard = new TransformWizard(getHeatmap());

        WizardDialog wizDlg = new WizardDialog(Application.get(), wizard);
        wizDlg.open();
        if (wizDlg.isCancelled()) {
            return;
        }

        //final



        final JobRunnable transformer2 = new JobRunnable() {

            @Override
            public void run(IProgressMonitor monitor) throws Exception {

                // Launch wizard
                List<ConfigurableTransformFunction> transformFunctions = new ArrayList<>();
                IMatrixLayer layer = wizard.getLayer();
                final HeatmapLayer newLayer = new HeatmapLayer(wizard.getNewLayer());

                IMatrixPosition matrixPointer = heatmap.newPosition();

                HashMatrix mainData = (HashMatrix) heatmap.getContents();
                mainData.addLayer(newLayer);
                heatmap.getLayers().initLayer(newLayer);

                // Copy data first

                transformFunctions.addAll(wizard.getFunctions());
                monitor.subtask().begin("Copying data for transformation", 1);
                copyLayerValues(heatmap, layer, heatmap, newLayer);

                // Transform

                for (TransformFunction transformFunction : transformFunctions) {
                    matrixPointer.iterate(layer)
                            .monitor(monitor, "<html><body>Applying data transformation <b>'" + transformFunction.getName() + "'</b></html></body>")
                            .transform(transformFunction)
                            .store(heatmap, newLayer);
                    if (newLayer != layer) {
                        layer = newLayer;
                    }
                }

                monitor.subtask().begin("Preparing new data", 1);

                IMatrixIterable<Double> matrixIterable = heatmap.newPosition()
                        .iterate(newLayer, heatmap.getRows(), heatmap.getColumns());

                final Double max = MaxAggregator.INSTANCE.aggregate(matrixIterable);
                final Double min = MinAggregator.INSTANCE.aggregate(matrixIterable);
                final Double median = MedianAggregator.INSTANCE.aggregate(matrixIterable);

                monitor.end();

                heatmap.getLayers().updateLayers();
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        LinearDecorator decorator = new LinearDecorator();
                        decorator.setMinValue(min);
                        decorator.setMidValue(median);
                        decorator.setMaxValue(max);
                        HeatmapLayer finalNovelLayer = heatmap.getLayers().get(newLayer.getId());
                        finalNovelLayer.setDecorator(decorator);
                        getHeatmap().getLayers().setTopLayer(finalNovelLayer);
                    }
                };
                Executors.newSingleThreadScheduledExecutor().schedule(task, 10, TimeUnit.MILLISECONDS);
                Application.get().showNotification("New data layer added");
            }
        };



        JobThread.execute(Application.get(), transformer2);

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

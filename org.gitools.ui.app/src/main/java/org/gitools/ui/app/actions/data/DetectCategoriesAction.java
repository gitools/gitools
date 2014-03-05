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
package org.gitools.ui.app.actions.data;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;


public class DetectCategoriesAction extends HeatmapAction {

    private ArrayList<Double> categories;

    public DetectCategoriesAction() {
        super("<html><i>Detect</i> layer categories</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        run();
    }

    public void run() {
        final Heatmap heatmap = getHeatmap();
        final ArrayList<Double> categories = new ArrayList<>();

        JobThread.execute(Application.get(), new JobRunnable() {

            @Override
            public void run(IProgressMonitor monitor) {

                try {
                    monitor.begin("Detecting categories ...", heatmap.getRows().size());

                    IMatrixPosition position = heatmap.newPosition();
                    for (String row : position.iterate(heatmap.getRows()).monitor(monitor)) {
                        for (Object v : position.iterate(heatmap.getLayers().getTopLayer(), heatmap.getColumns())) {

                            if (v != null && !categories.contains(v)) {
                                categories.add((Double) v);
                            }
                        }
                        if (categories.size() > 30) {
                            break;
                        }
                    }

                } catch (CancellationException e) {
                    categories.clear();
                }
            }
        });

        this.categories = categories;
        Application.get().setStatusText("Sort done.");
    }

    public ArrayList<Double> getCategories() {
        return categories;
    }
}

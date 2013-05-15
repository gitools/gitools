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
package org.gitools.ui.actions.data;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.label.LabelProvider;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.sort.MatrixViewSorter;
import org.gitools.core.matrix.sort.ValueSortCriteria;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;


public class SortByHeaderAction extends BaseAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public SortByHeaderAction() {
        super("Sort by header");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (header == null) {
            return;
        }

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);

                HeatmapDimension dimension = header.getHeatmapDimension();
                LabelProvider labelProvider = header.getLabelProvider();
                boolean numeric = header.isNumeric();
                boolean ascending = header.isSortAscending();
                dimension.setVisibleIndices(MatrixViewSorter.sortLabels(labelProvider, (ascending ? ValueSortCriteria.SortDirection.ASCENDING : ValueSortCriteria.SortDirection.DESCENDING), dimension.getVisibleIndices(), numeric));

                // Invert sort direction for the next time
                header.setSortAscending(!ascending);

                monitor.end();
            }
        });

        AppFrame.get().setStatusText("Sort done.");
    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        this.header = header;

        if (header instanceof HeatmapDecoratorHeader) {
            ((HeatmapDecoratorHeader) header).setSortLabel(position.getHeaderLabel());
        }

        setName("Sort " + (header.isSortAscending()?"ascending":"descending") + " by '" + header.getTitle() + "'");
    }
}

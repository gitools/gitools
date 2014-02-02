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
import static org.gitools.api.matrix.SortDirection.ASCENDING;
import static org.gitools.api.matrix.SortDirection.DESCENDING;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.sort.SortByLabelComparator;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.app.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;


public class SortByHeaderAction extends HeatmapAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public SortByHeaderAction() {
        super("Sort by header");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (header == null) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);

                HeatmapDimension dimension = header.getHeatmapDimension();

                dimension.sort(new SortByLabelComparator(
                        header.isSortAscending() ? ASCENDING : DESCENDING,
                        header.getIdentifierTransform(),
                        header.isNumeric()
                ));

                monitor.end();
            }
        });

        Application.get().setStatusText("Sort done.");
    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        this.header = header;

        if (header instanceof HeatmapDecoratorHeader) {
            ((HeatmapDecoratorHeader) header).setSortLabel(position.getHeaderAnnotation());
        }

        setName("Sort " + (header.isSortAscending() ? "ascending" : "descending") + " by '" + header.getTitle() + "'");
    }
}

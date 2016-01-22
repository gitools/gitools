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

import com.google.common.base.Function;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;

import static org.gitools.api.matrix.SortDirection.ASCENDING;
import static org.gitools.api.matrix.SortDirection.DESCENDING;


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

        String sortedBy = header.isSortAscending() ? ASCENDING.toString() : DESCENDING.toString();


        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);

                HeatmapDimension dimension = header.getHeatmapDimension();
                MatrixViewSorter.sortByLabel(dimension,
                        header.isSortAscending() ? ASCENDING : DESCENDING,
                        header.getSortAnnotationPattern(),
                        header instanceof HeatmapDecoratorHeader);
                header.setSortAscending(!header.isSortAscending());

            }
        });

        Application.get().showNotification("Annotation sorting applied (" + sortedBy + ")");
    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        this.header = header;

        setEnabled(!(header instanceof HierarchicalClusterHeatmapHeader));

        if (header instanceof HeatmapDecoratorHeader) {
            ((HeatmapDecoratorHeader) header).setSortLabel(position.getHeaderAnnotation());
        }

        HeatmapDimension sortDimension = header.getHeatmapDimension();
        String dimension = sortDimension.getId().getLabel();
        String which = sortDimension.getSelected().size() > 0 ?  sortDimension.getSelected().size() + "" : "all";

        setName("<html><i>Sort</i> " + which + " " + dimension + "s " + (header.isSortAscending() ? "asc." : "des.") + " by <b>" + header.getTitle() + "</b></html>");
    }

    private class ToLowerCaseFunction implements Function<String, String> {

        private Function<String, String> innerFunction;

        public ToLowerCaseFunction(Function<String, String> innerFunction) {
            this.innerFunction = innerFunction;
        }

        @Override
        public String apply(String input) {

            String out = innerFunction.apply(input);

            return (out == null ? null : out.toLowerCase());
        }
    }
}

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
package org.gitools.ui.core.commands;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.MatrixViewSorter;

public abstract class HeaderCommand extends HeatmapCommand {

    protected final MatrixDimensionKey dimension;
    protected final String COLUMNS = "COLUMNS";
    protected final String ROWS = "ROWS";

    protected HeatmapDimension heatmapDimension;
    protected SortDirection sort;
    protected String pattern;


    public HeaderCommand(String heatmap, MatrixDimensionKey dimension, String sort, String pattern) {
        super(heatmap);
        this.dimension = dimension;
        this.pattern = pattern;
        if (sort != null) {
            if (sort.toLowerCase().contains("asc")) {
                this.sort = SortDirection.ASCENDING;
            } else if (sort.toLowerCase().contains("desc")) {
                this.sort = SortDirection.DESCENDING;
            }
        }
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {


        super.execute(monitor);
        heatmapDimension = heatmap.getDimension(dimension);

        return;
    }

    protected void applySort() {
        if (sort != null) {
            MatrixViewSorter.sortByLabel(heatmap, heatmapDimension.getId().equals(MatrixDimensionKey.ROWS), pattern, sort,
                    false, heatmapDimension.getId().equals(MatrixDimensionKey.COLUMNS), pattern, sort, false);
        }
    }
}

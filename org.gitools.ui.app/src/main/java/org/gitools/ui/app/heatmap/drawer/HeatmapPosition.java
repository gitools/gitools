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
package org.gitools.ui.app.heatmap.drawer;

import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixPosition;
import org.gitools.heatmap.decorator.Decoration;

public class HeatmapPosition extends MatrixPosition implements IMatrixPosition {

    public int row;
    public int column;

    public String headerAnnotation;
    public Decoration headerDecoration;

    public HeatmapPosition(Heatmap heatmap) {
        super(heatmap);
    }

    public HeatmapPosition(Heatmap heatmap, int row, int column) {
        this(heatmap, row, column, null, null);
    }

    public HeatmapPosition(Heatmap heatmap, int row, int column, String headerAnnotation) {
        this(heatmap, row, column, headerAnnotation, null);
    }

    public HeatmapPosition(Heatmap heatmap, int row, int column, String headerAnnotation, Decoration headerDecoration) {
        super(heatmap);

        set(heatmap.getRows().getLabel(row), heatmap.getColumns().getLabel(column));

        this.row = row;
        this.column = column;
        this.headerAnnotation = headerAnnotation;
        this.headerDecoration = headerDecoration;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getHeaderAnnotation() {
        return headerAnnotation;
    }

}

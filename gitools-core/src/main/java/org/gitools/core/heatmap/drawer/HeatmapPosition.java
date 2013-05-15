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
package org.gitools.core.heatmap.drawer;

import org.jetbrains.annotations.NotNull;

public class HeatmapPosition {

    public int row;
    public int column;
    public String headerLabel;

    public HeatmapPosition() {
    }

    public HeatmapPosition(int row, int column) {
        this(row, column, null);
    }

    public HeatmapPosition(int row, int column, String headerLabel) {
        this.row = row;
        this.column = column;
        this.headerLabel = headerLabel;
    }

    HeatmapPosition(@NotNull HeatmapPosition pos) {
        this.row = pos.row;
        this.column = pos.column;
        this.headerLabel = pos.headerLabel;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getHeaderLabel() {
        return headerLabel;
    }
}

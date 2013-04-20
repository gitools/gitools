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
package org.gitools.analysis.groupcomparison;

import org.gitools.datafilters.BinaryCutoff;
import org.jetbrains.annotations.Nullable;

public class ColumnGroup {

    private String name = "";
    private int[] columns = new int[0];
    @Nullable
    private BinaryCutoff binaryCutoff = null;
    private int cutoffAttributeIndex = -1;

    public ColumnGroup(String string) {
        this.name = string;
    }

    public ColumnGroup(String name, int[] columns, BinaryCutoff binaryCutoff, int cutoffAttributeIndex) {
        this.name = name;
        this.columns = columns;
        this.binaryCutoff = binaryCutoff;
        this.cutoffAttributeIndex = cutoffAttributeIndex;
    }

    @Nullable
    public BinaryCutoff getBinaryCutoff() {
        return binaryCutoff;
    }

    public void setBinaryCutoff(BinaryCutoff binaryCutoff) {
        this.binaryCutoff = binaryCutoff;
    }

    public int getCutoffAttributeIndex() {
        return cutoffAttributeIndex;
    }

    public void setCutoffAttributeIndex(int cutoffAttributeIndex) {
        this.cutoffAttributeIndex = cutoffAttributeIndex;
    }

    public int[] getColumns() {
        return columns;
    }

    public void setColumns(int[] columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

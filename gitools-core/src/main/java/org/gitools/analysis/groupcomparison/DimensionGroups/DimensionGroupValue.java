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
package org.gitools.analysis.groupcomparison.DimensionGroups;

import org.gitools.analysis.groupcomparison.filters.GroupByValuePredicate;
import org.gitools.utils.datafilters.BinaryCutoff;

public class DimensionGroupValue extends DimensionGroup {

    private BinaryCutoff binaryCutoff = null;
    private int cutoffAttributeIndex = -1;


    public DimensionGroupValue(String name, GroupByValuePredicate predicate) {
        super(name, predicate, DimensionGroupEnum.Value);
    }

    //public DimensionGroupValue(String name, BinaryCutoff binaryCutoff, int cutoffAttributeIndex) {
    //    this(name);
    //    this.binaryCutoff = binaryCutoff;
    //    this.cutoffAttributeIndex = cutoffAttributeIndex;
    //}


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

    @Override
    public String getProperty() {
        return "some value";

    }
}

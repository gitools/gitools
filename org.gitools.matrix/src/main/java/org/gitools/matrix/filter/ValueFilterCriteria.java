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
package org.gitools.matrix.filter;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.utils.cutoffcmp.CutoffCmp;

public class ValueFilterCriteria {

    protected IMatrixLayer layer;
    protected CutoffCmp comparator;
    protected Double value;
    protected Double nullConversion;

    public ValueFilterCriteria(IMatrixLayer layer, CutoffCmp comparator, Double value, Double nullConversion) {
        this.layer = layer;
        this.comparator = comparator;
        this.value = value;
        this.nullConversion = nullConversion;
    }

    public IMatrixLayer getLayer() {
        return layer;
    }

    public void setLayer(IMatrixLayer layer) {
        this.layer = layer;
    }

    public CutoffCmp getComparator() {
        return comparator;
    }

    public void setComparator(CutoffCmp comparator) {
        this.comparator = comparator;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return layer.getId() + " " + comparator.toString() + " " + value;
    }

    public Double getNullConversion() {
        return nullConversion;
    }

    public void setNullConversion(Double nullConversion) {
        this.nullConversion = nullConversion;
    }
}

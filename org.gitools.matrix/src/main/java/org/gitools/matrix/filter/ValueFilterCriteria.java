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

import org.gitools.api.analysis.IAggregator;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.utils.cutoffcmp.CutoffCmp;

public class ValueFilterCriteria {

    private IMatrixLayer layer;
    private CutoffCmp comparator;
    private double value;
    private IAggregator aggregator;

    public ValueFilterCriteria(IMatrixLayer layer, CutoffCmp comparator, double value) {
        this.layer = layer;
        this.comparator = comparator;
        this.value = value;
        this.aggregator = null;
    }

    public ValueFilterCriteria(IMatrixLayer layer, IAggregator aggregator, CutoffCmp comparator, double value) {
        this.aggregator = aggregator;
        this.layer = layer;
        this.comparator = comparator;
        this.value = value;
    }

    public IMatrixLayer getLayer() {
        return layer;
    }

    public String getAttributeName() {
        return layer.getName();
    }

    public void setAttributeName(String attributeName) {
        //this.attributeName = attributeName;
    }

    public void setAttributeIndex(int attributeIndex) {
        //this.attributeIndex = attributeIndex;
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

    public void setValue(double value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return layer.getId() + " " + comparator.toString() + " " + value;
    }
}

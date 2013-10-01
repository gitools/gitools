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
package org.gitools.core.matrix.filter;

import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;

public class ValueFilterCriteria {

    private String attributeName;
    private int attributeIndex;
    private CutoffCmp comparator;
    private double value;
    private IAggregator aggregator;

    public ValueFilterCriteria(String attributeName, int attributeIndex, CutoffCmp comparator, double value) {
        this.attributeName = attributeName;
        this.attributeIndex = attributeIndex;
        this.comparator = comparator;
        this.value = value;
        this.aggregator = null;
    }

    public ValueFilterCriteria(String attributeName, int attributeIndex, IAggregator aggregator, CutoffCmp comparator, double value) {
        this.aggregator = aggregator;
        this.attributeName = attributeName;
        this.attributeIndex = attributeIndex;
        this.comparator = comparator;
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(int attributeIndex) {
        this.attributeIndex = attributeIndex;
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

    @NotNull
    @Override
    public String toString() {
        return attributeName.toString() + " " + comparator.toString() + " " + value;
    }
}

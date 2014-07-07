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
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPredicate;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.xml.adapter.CutoffCmpXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class ValueFilterFunction implements IMatrixPredicate<Double> {

    protected String layerId;

    @XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
    protected CutoffCmp comparator;

    protected Double cutoffValue;
    protected Double nullConversion;

    public ValueFilterFunction() {
        //JAXB requirement
    }

    public ValueFilterFunction(IMatrixLayer layer, CutoffCmp comparator, Double cutoffValue, Double nullConversion) {
        this.layerId = layer.getId();
        this.comparator = comparator;
        this.cutoffValue = cutoffValue;
        this.nullConversion = nullConversion;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayer(IMatrixLayer layer) {
        this.layerId = layer.getId();
    }

    public void setLayer(String layerId) {
        this.layerId = layerId;
    }

    public CutoffCmp getComparator() {
        return comparator;
    }

    public void setComparator(CutoffCmp comparator) {
        this.comparator = comparator;
    }

    public double getCutoffValue() {
        return this.cutoffValue;
    }

    public void setCutoffValue(Double cutoffValue) {
        this.cutoffValue = cutoffValue;
    }

    @Override
    public String toString() {
        return layerId + " " + comparator.toString() + " " + cutoffValue;
    }

    public Double getNullConversion() {
        return nullConversion;
    }

    public void setNullConversion(Double nullConversion) {
        this.nullConversion = nullConversion;
    }

    @Override
    public boolean apply(Double value, IMatrixPosition position) {

        if (value == null) {
            if (nullConversion == null) {
                return false;
            } else {
                value = nullConversion;
            }
        }

        return comparator.compare(value, cutoffValue);

    }
}

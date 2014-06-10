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
package org.gitools.analysis.overlapping;

import org.gitools.analysis.Analysis;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.xml.adapter.CutoffCmpXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OverlappingAnalysis extends Analysis {

    /**
     * If different from null then replace NaN values by a number
     */
    private Double replaceNanValue;

    /**
     * If true then compare rows instead of columns
     */
    private boolean transposeData;

    /**
     * Data attribute name
     */
    private String attributeName;

    /**
     * Data binary cutoff enabled
     */
    private boolean binaryCutoffEnabled;

    /**
     * Data binary cutoff comparator
     */
    @XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
    private CutoffCmp binaryCutoffCmp;

    /**
     * Data binary cutoff value
     */
    private Double binaryCutoffValue;

    public ResourceReference<IMatrix> getCellResults() {
        return cellResults;
    }

    public void setCellResults(ResourceReference<IMatrix> cellResults) {
        this.cellResults = cellResults;
    }

    public ResourceReference<IMatrix> getFilteredData() {
        return filteredData;
    }

    public void setFilteredData(ResourceReference<IMatrix> filteredData) {
        this.filteredData = filteredData;
    }

    public ResourceReference<IMatrix> getSourceData() {
        return sourceData;
    }

    public void setSourceData(ResourceReference<IMatrix> sourceData) {
        this.sourceData = sourceData;
    }

    /**
     * Data source file
     */
    @XmlElement
    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> sourceData;

    /**
     * Filtered data file
     */
    @XmlElement
    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> filteredData;

    /**
     * Results
     */
    @XmlElement
    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> cellResults;

    public OverlappingAnalysis() {
        this.transposeData = false;
    }

    public Double getReplaceNanValue() {
        return replaceNanValue;
    }

    public void setReplaceNanValue(Double replaceNanValue) {
        this.replaceNanValue = replaceNanValue;
    }

    public boolean isTransposeData() {
        return transposeData;
    }

    public void setTransposeData(boolean transposeData) {
        this.transposeData = transposeData;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public boolean isBinaryCutoffEnabled() {
        return binaryCutoffEnabled;
    }

    public void setBinaryCutoffEnabled(boolean binaryCutoffEnabled) {
        this.binaryCutoffEnabled = binaryCutoffEnabled;
    }

    public CutoffCmp getBinaryCutoffCmp() {
        return binaryCutoffCmp;
    }

    public void setBinaryCutoffCmp(CutoffCmp binaryCutoffCmp) {
        this.binaryCutoffCmp = binaryCutoffCmp;
    }

    public Double getBinaryCutoffValue() {
        return binaryCutoffValue;
    }

    public void setBinaryCutoffValue(Double binaryCutoffValue) {
        this.binaryCutoffValue = binaryCutoffValue;
    }

}

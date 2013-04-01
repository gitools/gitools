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

import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.model.ResourceRef;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.xml.adapter.CutoffCmpXmlAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OverlappingAnalysis extends Analysis
{

    /**
     * If different from null then replace NaN values by a number
     */
    protected Double replaceNanValue;

    /**
     * If true then compare rows instead of columns
     */
    protected boolean transposeData;

    /**
     * Data attribute name
     */
    protected String attributeName;

    /**
     * Data binary cutoff enabled
     */
    protected boolean binaryCutoffEnabled;

    /**
     * Data binary cutoff comparator
     */
    @XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
    protected CutoffCmp binaryCutoffCmp;

    /**
     * Data binary cutoff value
     */
    protected Double binaryCutoffValue;

    /**
     * Data source file
     */
    @XmlElement(name = "sourceData")
    protected ResourceRef sourceDataResource;

    /**
     * Filtered data file
     */
    @XmlElement(name = "filteredData")
    protected ResourceRef filteredDataResource;

    /**
     * Data
     */
    @XmlTransient
    protected IMatrix data;

    /**
     * Cell Results file
     */
    @XmlElement(name = "cellResults")
    protected ResourceRef cellResultsResource;

    /**
     * Results
     */
    @XmlTransient
    protected IMatrix cellResults;

    public OverlappingAnalysis()
    {
        this.transposeData = false;
    }

    public Double getReplaceNanValue()
    {
        return replaceNanValue;
    }

    public void setReplaceNanValue(Double replaceNanValue)
    {
        this.replaceNanValue = replaceNanValue;
    }

    public boolean isTransposeData()
    {
        return transposeData;
    }

    public void setTransposeData(boolean transposeData)
    {
        this.transposeData = transposeData;
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }

    public boolean isBinaryCutoffEnabled()
    {
        return binaryCutoffEnabled;
    }

    public void setBinaryCutoffEnabled(boolean binaryCutoffEnabled)
    {
        this.binaryCutoffEnabled = binaryCutoffEnabled;
    }

    public CutoffCmp getBinaryCutoffCmp()
    {
        return binaryCutoffCmp;
    }

    public void setBinaryCutoffCmp(CutoffCmp binaryCutoffCmp)
    {
        this.binaryCutoffCmp = binaryCutoffCmp;
    }

    public Double getBinaryCutoffValue()
    {
        return binaryCutoffValue;
    }

    public void setBinaryCutoffValue(Double binaryCutoffValue)
    {
        this.binaryCutoffValue = binaryCutoffValue;
    }

    public ResourceRef getSourceDataResource()
    {
        return sourceDataResource;
    }

    public void setSourceDataResource(ResourceRef sourceDataResource)
    {
        this.sourceDataResource = sourceDataResource;
    }

    public ResourceRef getFilteredDataResource()
    {
        return filteredDataResource;
    }

    public void setFilteredDataResource(ResourceRef filteredDataResource)
    {
        this.filteredDataResource = filteredDataResource;
    }

    public IMatrix getData()
    {
        return data;
    }

    public void setData(IMatrix data)
    {
        this.data = data;
    }

    public ResourceRef getCellResultsResource()
    {
        return cellResultsResource;
    }

    public void setCellResultsResource(ResourceRef cellResultsResource)
    {
        this.cellResultsResource = cellResultsResource;
    }

    @Deprecated
    public IMatrix getCellResults()
    {
        return cellResults;
    }

    public IMatrix getResult()
    {
        return cellResults;
    }

    public void setCellResults(IMatrix results)
    {
        this.cellResults = results;
    }
}

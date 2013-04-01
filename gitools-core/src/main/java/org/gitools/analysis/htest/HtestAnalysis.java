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
package org.gitools.analysis.htest;

import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.Analysis;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.xml.adapter.CutoffCmpXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement

@XmlSeeAlso({
        EnrichmentAnalysis.class,
        OncodriveAnalysis.class})

@XmlAccessorType(XmlAccessType.FIELD)
public class HtestAnalysis extends Analysis
{

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
    protected double binaryCutoffValue;

    /**
     * Test name
     */
    protected ToolConfig testConfig;

    /**
     * Modules
     */
    @XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
    protected ModuleMap moduleMap;

    /**
     * Minimum module size
     */
    protected int minModuleSize;

    /**
     * Maximum module size
     */
    protected int maxModuleSize;

    /**
     * Data
     */
    @XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
    //@XmlElement(name = "dataMatrix")
    protected IMatrix data;

    /**
     * Multiple test correction
     */
    protected String mtc;

    /**
     * Results
     */
    @XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
    //@XmlElement(name = "resultsMatrix")
    protected ObjectMatrix results; //FIXME Should it be a BaseMatrix or IMatrix instead ?

    public HtestAnalysis()
    {
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

    public void setBinaryCutoffCmp(CutoffCmp comparator)
    {
        this.binaryCutoffCmp = comparator;
    }

    public double getBinaryCutoffValue()
    {
        return binaryCutoffValue;
    }

    public void setBinaryCutoffValue(double value)
    {
        this.binaryCutoffValue = value;
    }

    public ToolConfig getTestConfig()
    {
        return testConfig;
    }

    public void setTestConfig(ToolConfig testConfig)
    {
        this.testConfig = testConfig;
    }

    public ModuleMap getModuleMap()
    {
        return moduleMap;
    }

    public void setModuleMap(ModuleMap moduleMap)
    {
        this.moduleMap = moduleMap;
    }

    public int getMinModuleSize()
    {
        return minModuleSize;
    }

    public void setMinModuleSize(int minModuleSize)
    {
        this.minModuleSize = minModuleSize;
    }

    public int getMaxModuleSize()
    {
        return maxModuleSize;
    }

    public void setMaxModuleSize(int maxModuleSize)
    {
        this.maxModuleSize = maxModuleSize;
    }

    public IMatrix getData()
    {
        return data;
    }

    public void setData(IMatrix data)
    {
        this.data = data;
    }

    public String getMtc()
    {
        return mtc;
    }

    public void setMtc(String mtc)
    {
        this.mtc = mtc;
    }

    public ObjectMatrix getResults()
    {
        return results;
    }

    public void setResults(ObjectMatrix results)
    {
        this.results = results;
    }
}

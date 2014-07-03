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

import org.gitools.analysis.Analysis;
import org.gitools.analysis.ToolConfig;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;
import org.gitools.matrix.geneset.GeneSet;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.xml.adapter.CutoffCmpXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlSeeAlso({EnrichmentAnalysis.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class HtestAnalysis extends Analysis {

    private boolean binaryCutoffEnabled;

    @XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
    private CutoffCmp binaryCutoffCmp;

    private double binaryCutoffValue;

    private ToolConfig testConfig;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IModuleMap> moduleMap;

    private int minModuleSize;

    private int maxModuleSize;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;

    private String layer;

    private String mtc;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<GeneSet> population;

    private Double populationDefaultValue;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> results;

    protected HtestAnalysis() {
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

    public void setBinaryCutoffCmp(CutoffCmp comparator) {
        this.binaryCutoffCmp = comparator;
    }

    public double getBinaryCutoffValue() {
        return binaryCutoffValue;
    }

    public void setBinaryCutoffValue(double value) {
        this.binaryCutoffValue = value;
    }

    public ToolConfig getTestConfig() {
        return testConfig;
    }

    public void setTestConfig(ToolConfig testConfig) {
        this.testConfig = testConfig;
    }

    public ResourceReference<IModuleMap> getModuleMap() {
        return moduleMap;
    }

    public void setModuleMap(ResourceReference<IModuleMap> moduleMap) {
        this.moduleMap = moduleMap;
    }

    public int getMinModuleSize() {
        return minModuleSize;
    }

    public void setMinModuleSize(int minModuleSize) {
        this.minModuleSize = minModuleSize;
    }

    public int getMaxModuleSize() {
        return maxModuleSize;
    }

    public void setMaxModuleSize(int maxModuleSize) {
        this.maxModuleSize = maxModuleSize;
    }

    public ResourceReference<IMatrix> getData() {
        return data;
    }

    public void setData(ResourceReference<IMatrix> data) {
        this.data = data;
    }

    public String getLayer() {

        if (layer == null) {
            return getData().get().getLayers().iterator().next().getId();
        }

        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getMtc() {
        return mtc;
    }

    public void setMtc(String mtc) {
        this.mtc = mtc;
    }

    public ResourceReference<IMatrix> getResults() {
        return results;
    }

    public void setResults(ResourceReference<IMatrix> results) {
        this.results = results;
    }

    public ResourceReference<GeneSet> getPopulation() {
        return population;
    }

    public void setPopulation(ResourceReference<GeneSet> population) {
        this.population = population;
    }

    public Double getPopulationDefaultValue() {
        return populationDefaultValue;
    }

    public void setPopulationDefaultValue(Double populationDefaultValue) {
        this.populationDefaultValue = populationDefaultValue;
    }
}

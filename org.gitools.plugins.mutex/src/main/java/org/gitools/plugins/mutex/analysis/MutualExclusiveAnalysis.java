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
package org.gitools.plugins.mutex.analysis;

import org.gitools.analysis.Analysis;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;
import org.gitools.heatmap.decorator.impl.NonEventToNullFunction;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class MutualExclusiveAnalysis extends Analysis {

    private String layer;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> results;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IModuleMap> testGroupsModuleMap;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IModuleMap> weightGroupsModuleMap;

    private IMatrixDimension testDimension;

    private IMatrixDimension weightDimension;

    private int iterations = 10000;

    private NonEventToNullFunction eventFunction;

    public MutualExclusiveAnalysis() {
    }


    public IMatrixDimension getTestDimension() {
        return testDimension;
    }

    public void setTestDimension(IMatrixDimension testDimension) {
        this.testDimension = testDimension;
    }

    public ResourceReference<IModuleMap> getTestGroupsModuleMap() {
        return testGroupsModuleMap;
    }

    public void setTestGroupsModuleMap(ResourceReference<IModuleMap> testGroupsModuleMap) {
        this.testGroupsModuleMap = testGroupsModuleMap;
    }

    public void setTestGroupsModuleMap(IModuleMap testGroupsModuleMap) {
        setTestGroupsModuleMap(new ResourceReference<>("testGroupsModuleMap", testGroupsModuleMap));
    }

    public ResourceReference<IModuleMap> getWeightGroupsModuleMap() {
        return weightGroupsModuleMap;
    }

    public void setWeightGroupsModuleMap(ResourceReference<IModuleMap> weightGroupsModuleMap) {
        this.weightGroupsModuleMap = weightGroupsModuleMap;
    }

    public void setWeightGroupsModuleMap(IModuleMap weightGroupsModuleMap) {
        setWeightGroupsModuleMap(new ResourceReference<>("weightGroupsModuleMap", weightGroupsModuleMap));
    }

    public ResourceReference<IMatrix> getData() {
        return data;
    }

    public void setData(ResourceReference<IMatrix> data) {
        this.data = data;
    }

    public void setData(IMatrix data) {
        setData(new ResourceReference<>("data", data));
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

    public ResourceReference<IMatrix> getResults() {
        return results;
    }

    public void setResults(ResourceReference<IMatrix> results) {
        this.results = results;
    }

    public IMatrixDimension getWeightDimension() {
        return weightDimension;
    }

    public void setWeightDimension(IMatrixDimension weightDimension) {
        this.weightDimension = weightDimension;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public NonEventToNullFunction getEventFunction() {
        return eventFunction;
    }

    public void setEventFunction(NonEventToNullFunction eventFunction) {
        this.eventFunction = eventFunction;
    }
}

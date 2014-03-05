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
package org.gitools.analysis.groupcomparison;

import org.gitools.analysis.Analysis;
import org.gitools.analysis.ToolConfig;
import org.gitools.analysis.groupcomparison.dimensiongroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.dimensiongroups.DimensionGroupEnum;
import org.gitools.analysis.stats.test.Test;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.ResourceReference;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GroupComparisonAnalysis extends Analysis implements Serializable {
    private String sizeAttrName;

    private String pvalueAttrName;

    private boolean transposeData;

    private String layerName;

    private DimensionGroupEnum columnGrouping = null;

    protected String dataFile = "";

    @XmlTransient
    private DimensionGroupEnum columnGroupType;

    @XmlTransient
    private boolean copyAnnotation = false;

    private List<DimensionGroup> groups;

    private ToolConfig testConfig;

    private String mtc;

    private ResourceReference<? extends IMatrix> data;

    private ResourceReference<? extends IMatrix> results;
    private Double nullConversion;


    public String getMtc() {
        return mtc;
    }

    public void setMtc(String mtc) {
        this.mtc = mtc;
    }

    public void setToolConfig(ToolConfig testConfig) {
        this.testConfig = testConfig;
    }

    public Test getTest() {
        TestFactory tf = TestFactory.createFactory(testConfig);
        return tf.create();
    }

    public GroupComparisonAnalysis() {
        this.transposeData = false;
        this.nullConversion = Double.NaN;
        this.groups = new ArrayList<>();
    }

    public String getSizeAttrName() {
        return sizeAttrName;
    }

    public void setLayer(String layerName) {
        this.layerName = layerName;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setSizeAttrName(String sizeAttrName) {
        this.sizeAttrName = sizeAttrName;
    }

    public String getPvalueAttrName() {
        return pvalueAttrName;
    }

    public void setPvalueAttrName(String pvalueAttrName) {
        this.pvalueAttrName = pvalueAttrName;
    }

    public boolean isTransposeData() {
        return transposeData;
    }

    public void setTransposeData(boolean transposeData) {
        this.transposeData = transposeData;
    }


    public DimensionGroupEnum getColumnGrouping() {
        return columnGrouping;
    }


    public void setColumnGrouping(DimensionGroupEnum columnGrouping) {
        this.columnGrouping = columnGrouping;
    }

    public DimensionGroup getGroup(int index) {
        return groups.get(index);
    }

    public List<DimensionGroup> getGroups() {
        return groups;
    }

    public void setGroup(DimensionGroup group, int index) {
        this.groups.set(index, group);
    }

    public void addGroups(List<DimensionGroup> groups) {
        for (DimensionGroup g : groups) {
            this.groups.add(g);
        }
    }

    public ResourceReference<? extends IMatrix> getData() {
        return data;
    }


    public void setData(ResourceReference<IMatrix> data) {
        this.data = data;
    }

    public ResourceReference<? extends IMatrix> getResults() {
        return results;
    }

    public void setResults(ResourceReference<? extends IMatrix> results) {
        this.results = results;
    }

    public Double getNullConversion() {
        return nullConversion;
    }

    public void setNullConversion(Double nullConversion) {
        this.nullConversion = nullConversion;
    }

    public DimensionGroupEnum getColumnGroupType() {
        return columnGroupType;
    }

    public void setColumnGroupType(DimensionGroupEnum columnGroupType) {
        this.columnGroupType = columnGroupType;
    }

    public boolean isCopyAnnotation() {
        return copyAnnotation;
    }

    public void setCopyAnnotation(boolean copyAnnotation) {
        this.copyAnnotation = copyAnnotation;
    }
}

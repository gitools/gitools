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
package org.gitools.core.analysis.groupcomparison;

import org.gitools.utils.datafilters.BinaryCutoff;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.IAnnotations;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.model.Analysis;
import org.gitools.core.model.ToolConfig;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.stats.mtc.MTC;
import org.gitools.core.stats.mtc.MTCFactory;
import org.gitools.core.stats.test.Test;
import org.gitools.core.stats.test.factory.TestFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GroupComparisonAnalysis extends Analysis implements Serializable {
    private String sizeAttrName;

    private String pvalueAttrName;

    private boolean transposeData;

    private int attributeIndex;

    @NotNull
    public static final String COLUMN_GROUPING_BY_VALUE = "Group by value";
    @NotNull
    public static final String COLUMN_GROUPING_BY_LABEL = "Group by label";

    @Nullable
    private String columnGrouping = null;

    @NotNull
    protected String dataFile = "";

    @XmlTransient
    private IAnnotations rowAnnotations;

    @XmlTransient
    private List<HeatmapHeader> rowHeaders;

    @XmlTransient
    private List<HeatmapHeader> columnHeaders;

    @XmlTransient
    private IAnnotations columnAnnotations;

    @XmlTransient
    private ColumnGroup group1;
    @XmlTransient
    private ColumnGroup group2;

    private ToolConfig testConfig;

    private String mtc;

    private ResourceReference<IMatrix> data;

    private ResourceReference<IMatrix> results;
    private double noneConversion;

    @Nullable
    public MTC getMtc() {
        return MTCFactory.createFromName(mtc);
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
        group1 = new ColumnGroup("Group 1");
        group2 = new ColumnGroup("Group 2");
        this.noneConversion = Double.NaN;
    }

    public String getSizeAttrName() {
        return sizeAttrName;
    }

    public void setAttributeIndex(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public int getAttributeIndex() {
        return attributeIndex;
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

    @Nullable
    public String getColumnGrouping() {
        return columnGrouping;
    }

    @NotNull
    public static String[] getColumnGroupingMethods() {
        return new String[]{COLUMN_GROUPING_BY_LABEL, COLUMN_GROUPING_BY_VALUE};
    }

    public void setColumnGrouping(String columnGrouping) {
        this.columnGrouping = columnGrouping;
    }

    public ColumnGroup getGroups1() {
        return group1;
    }

    public ColumnGroup getGroups2() {
        return group2;
    }

    public void setGroup1(Set<String> group1) {
        this.group1.setColumns(group1);
    }

    public void setGroup2(Set<String> group2) {
        this.group2.setColumns(group2);
    }

    public void setGroup1(BinaryCutoff binaryCutoff, int cutoffAttrIndex) {
        this.group1.setBinaryCutoff(binaryCutoff);
        this.group1.setCutoffAttributeIndex(cutoffAttrIndex);
    }

    public void setGroup2(BinaryCutoff binaryCutoff, int cutoffAttrIndex) {
        this.group2.setBinaryCutoff(binaryCutoff);
        this.group2.setCutoffAttributeIndex(cutoffAttrIndex);
    }

    public void setGroup1(ColumnGroup group1) {
        this.group1 = group1;
    }

    public void setGroup2(ColumnGroup group2) {
        this.group2 = group2;
    }

    public ResourceReference<IMatrix> getData() {
        return data;
    }


    public void setData(ResourceReference<IMatrix> data) {
        this.data = data;
    }

    public ResourceReference<IMatrix> getResults() {
        return results;
    }

    public void setResults(ResourceReference<IMatrix> results) {
        this.results = results;
    }

    public void setRowAnnotations(IAnnotations annotations) {
        this.rowAnnotations = annotations;
    }

    public IAnnotations getRowAnnotations() {
        return this.rowAnnotations;
    }

    public IAnnotations getColumnAnnotations() {
        return columnAnnotations;
    }

    public void setColumnAnnotations(IAnnotations columnAnnotations) {
        this.columnAnnotations = columnAnnotations;
    }

    public List<HeatmapHeader> getRowHeaders() {
        return rowHeaders;
    }

    public void setRowHeaders(List<HeatmapHeader> rowHeaders) {
        this.rowHeaders = rowHeaders;
    }

    public List<HeatmapHeader> getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(List<HeatmapHeader> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public double getNoneConversion() {
        return noneConversion;
    }

    public void setNoneConversion(double noneConversion) {
        this.noneConversion = noneConversion;
    }

}

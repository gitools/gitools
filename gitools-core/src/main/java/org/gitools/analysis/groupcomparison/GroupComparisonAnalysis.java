/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.analysis.groupcomparison;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.stats.test.Test;

@XmlRootElement
public class GroupComparisonAnalysis extends Analysis {


    protected String sizeAttrName;
	//protected int sizeAttrIndex;

	protected String pvalueAttrName;
	//protected int pvalueAttrIndex;

	protected boolean transposeData;
	
	protected int attributeIndex;
	//which attribute of the matrix should be taken as value

	public static String COLUMN_GROUPING_BY_VALUE = "Group by value";
	public static String COLUMN_GROUPING_BY_LABEL = "Group by label";

	protected String columnGrouping = null;

	protected String dataFile = "";

	protected AnnotationMatrix rowAnnotations;

	protected List<HeatmapHeader> rowHeaders;
	
	protected List<HeatmapHeader> columnHeaders;

	protected AnnotationMatrix columnAnnotations;


	protected ColumnGroup group1;
	protected ColumnGroup group2;

	protected Test test = new MannWhitneyWilxoxonTest();
	protected MTC mtc;


	/** Data */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix data;

	/** Results */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix results;

	public MTC getMtc() {
		return mtc;
	}

	public void setMtc(MTC mtc) {
		this.mtc = mtc;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public Test getTest() {
		return test;
	}

	public GroupComparisonAnalysis() {
		this.transposeData = false;
        group1  = new ColumnGroup("Group 1");
        group2  = new ColumnGroup("Group 2");
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
	
	public String getColumnGrouping() {
		return columnGrouping;
	}

	public static String[] getColumnGroupingMethods() {
		return new String[]{
			COLUMN_GROUPING_BY_LABEL,
			COLUMN_GROUPING_BY_VALUE
		};
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

	public void setGroup1(int[] group1) {
		this.group1.setColumns(group1);
	}

	public void setGroup2(int[] group2) {
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

	public IMatrix getData() {
		return data;
	}


	public void setData(IMatrix data) {
		this.data = data;
	}

	public IMatrix getResults() {
		return results;
	}

	public void setResults(IMatrix results) {
		this.results = results;
	}

	public void setRowAnnotations(AnnotationMatrix annotations) {
		this.rowAnnotations = annotations;
	}

	public AnnotationMatrix getRowAnnotations() {
		return this.rowAnnotations;
	}
	
	public AnnotationMatrix getColumnAnnotations() {
		return columnAnnotations;
	}

	public void setColumnAnnotations(AnnotationMatrix columnAnnotations) {
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
}

package org.gitools.matrix.filter;

import edu.upf.bg.cutoffcmp.CutoffCmp;

public class ValueFilterCriteria {

	protected String attributeName;
	protected int attributeIndex;
	protected CutoffCmp comparator;
	protected double value;

	public ValueFilterCriteria(String attributeName, int attributeIndex, CutoffCmp comparator, double value) {
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

	@Override
	public String toString() {
		return attributeName.toString() + " " + comparator.toString() + " " + value;
	}
}

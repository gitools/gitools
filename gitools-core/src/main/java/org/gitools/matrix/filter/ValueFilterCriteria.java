package org.gitools.matrix.filter;

import edu.upf.bg.cutoffcmp.CutoffCmp;

public class ValueFilterCriteria {

	protected ValueFilterCondition condition;
	
	protected String attributeName;
	protected int attributeIndex;
	protected CutoffCmp comparator;
	protected double value;

	public ValueFilterCriteria(String attributeName, int attributeIndex, ValueFilterCondition condition, double value) {
		this.attributeName = attributeName;
		this.attributeIndex = attributeIndex;
		this.condition = condition;
		this.value = value;
	}

	@Deprecated
	public ValueFilterCriteria(String attributeName, ValueFilterCondition condition, String value) {
		this(attributeName, -1, condition, Double.parseDouble(value));
	}

	public String getAttributeName() {
		return attributeName;
	}

	public int getAttributeIndex() {
		return attributeIndex;
	}

	public CutoffCmp getComparator() {
		return comparator;
	}

	public double getValue() {
		return this.value;
	}

	@Deprecated
	public ValueFilterCondition getCondition() {
		return this.condition;
	}

	@Override
	public String toString() {
		return attributeName.toString() + " " + condition.toString() + " " + value;
	}
}

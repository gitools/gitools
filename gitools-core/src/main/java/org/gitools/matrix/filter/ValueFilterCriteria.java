package org.gitools.matrix.filter;

import org.gitools._DEPRECATED.matrix.filter.ValueFilterCondition;
import edu.upf.bg.cutoffcmp.CutoffCmp;

public class ValueFilterCriteria {

	protected ValueFilterCondition condition;
	
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

	@Deprecated
	public ValueFilterCriteria(String attributeName, ValueFilterCondition condition, String value) {
		this(attributeName, -1, CutoffCmp.EQ, Double.parseDouble(value));
		this.condition = condition;
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

	@Deprecated
	public ValueFilterCondition getCondition() {
		return this.condition;
	}

	@Override
	public String toString() {
		return attributeName.toString() + " " + condition.toString() + " " + value;
	}
}

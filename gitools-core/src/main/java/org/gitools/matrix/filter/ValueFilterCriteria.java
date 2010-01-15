package org.gitools.matrix.filter;

public class ValueFilterCriteria {

	protected Object param;
	protected ValueFilterCondition condition;
	protected String value;

	public ValueFilterCriteria(Object param, ValueFilterCondition condition, String value) {
		this.param = param;
		this.condition = condition;
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public ValueFilterCondition getCondition() {
		return this.condition;
	}

	public Object getParam() {
		return this.param;
	}

	@Override
	public String toString() {
		return param.toString() + " " + condition.toString() + " " + value;
	}
}

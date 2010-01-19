package org.gitools._DEPRECATED.matrix.filter;

public enum ValueFilterCondition {

	GE("greater or equal"),
	LE("lower or equal"),
	GT("greater than"),
	LT("lower than"),
	EQ("equal"),
	NE("not equal");

	private String title;

	private ValueFilterCondition(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}
}

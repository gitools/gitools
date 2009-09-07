package org.gitools.datafilters;

import cern.colt.function.DoubleFunction;

public class BinaryCutoffParser	implements ValueParser {

	private static final long serialVersionUID = 4964176171201274622L;

	protected DoubleFunction filter;
	
	public BinaryCutoffParser(DoubleFunction filter) {
		this.filter = filter;
	}
	
	@Override
	public double parseValue(String str) {
		double value = Double.NaN;
		try {
			value = Double.parseDouble(str);
			value = filter.apply(value);
		}
		catch (NumberFormatException e) {}
		return value;
	}

}

package org.gitools.datafilters;

import cern.colt.function.DoubleFunction;

public class BinaryCutoffTranslator	implements ValueTranslator<Double> {

	private static final long serialVersionUID = 4964176171201274622L;

	protected DoubleFunction filter;
	
	public BinaryCutoffTranslator(DoubleFunction filter) {
		this.filter = filter;
	}
	
	@Override
	public Double stringToValue(String str) {
		double value = Double.NaN;
		try {
			value = Double.parseDouble(str);
			value = filter.apply(value);
		}
		catch (NumberFormatException e) {}
		return value;
	}

	@Override
	public String valueToString(Double value) {
		if (Double.isNaN(value))
			return "-";

		String str = String.valueOf(value);
		double frac = value - Math.floor(value);
		if (frac == 0.0)
			str = str.substring(0, str.length() - 2);
		return str;
	}

}

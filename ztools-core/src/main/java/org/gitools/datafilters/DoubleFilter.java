package org.gitools.datafilters;

public class DoubleFilter implements ValueFilter {

	@Override
	public double parseValue(String str) {
		double value = Double.NaN;
		try {
			value = Double.parseDouble(str);
		}
		catch (NumberFormatException e) {}
		return value;
	}

}

package org.gitools.datafilters;

public class DoubleTranslator implements ValueTranslator<Double> {

	@Override
	public Double stringToValue(String str) {
		double value = Double.NaN;
		try {
			value = Double.parseDouble(str);
		}
		catch (NumberFormatException e) {}
		return value;
	}

	@Override
	public String valueToString(Double value) {
		if (Double.isNaN(value))
			return "-";

		String s = String.valueOf(value);
		return s.endsWith(".0") ? s.substring(0, s.length() - 2) : s;
	}

}

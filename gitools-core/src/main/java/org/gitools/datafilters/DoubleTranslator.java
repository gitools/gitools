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
		return Double.isNaN(value) ?
			"-" : String.valueOf(value);
	}

}

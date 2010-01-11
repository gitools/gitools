package org.gitools.aggregation;

import cern.jet.math.Functions;

/** Sum of logarithms */
public class LogSumAggregator extends AbstractAggregator {

	@Override
	public double aggregate(double[] data) {
		return aggregate(data, Functions.plus, Functions.log, 0);
	}

	@Override
	public String toString() {
		return "Sum of logarithms";
	}
}

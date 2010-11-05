package org.gitools.aggregation;

import cern.jet.math.Functions;

/** Sum */
public class SumAggregator extends AbstractAggregator {

	@Override
	public double aggregate(double[] data) {
		return aggregate(data, Functions.plus, 0);
	}

	@Override
	public String toString() {
		return "Sum";
	}
}

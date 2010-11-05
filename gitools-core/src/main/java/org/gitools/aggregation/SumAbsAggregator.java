package org.gitools.aggregation;

import cern.jet.math.Functions;

/** Sum */
public class SumAbsAggregator extends AbstractAggregator {

	@Override
	public double aggregate(double[] data) {
		return aggregate(data, Functions.plusAbs, 0);
	}

	@Override
	public String toString() {
		return "Absolute Sum";
	}
}

package org.gitools.aggregation;

import cern.jet.math.Functions;

/** Multiplication */
public class MultAggregator extends AbstractAggregator {
	
	@Override
	public double aggregate(double[] data) {
		return aggregate(data, Functions.mult, 1);
	}

	@Override
	public String toString() {
		return "Multiplication";
	}
}

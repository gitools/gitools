package org.gitools.aggregation;

import cern.jet.math.Functions;

/** Mean */
public class MeanAggregator extends AbstractAggregator {
	
	@Override
	public double aggregate(double[] data) {
		double sum = aggregate(data, Functions.plus, 0);
		return sum / data.length; //FIXME should have into account NaN values ???
	}

	@Override
	public String toString() {
		return "Mean";
	}
}

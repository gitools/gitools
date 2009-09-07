package org.gitools.datafilters;

import java.io.Serializable;

import cern.colt.function.DoubleFunction;

public class BinaryCutoff implements DoubleFunction, Serializable {

	private static final long serialVersionUID = 5091376519840044515L;

	protected CutoffCmp cmp;
	protected double cutoff;
	
	public BinaryCutoff(CutoffCmp cmp, double cutoff) {
		this.cmp = cmp;
		this.cutoff = cutoff;
	}

	@Override
	public double apply(double value) {
		return Double.isNaN(value) ? Double.NaN :
			cmp.compare(value, cutoff) ? 1 : 0;
	}
}

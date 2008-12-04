package es.imim.bg.ztools.datafilters;

import cern.colt.function.DoubleFunction;

public class BinCutoffFilter 
		implements ValueFilter, DoubleFunction {

	public interface BinCutoffCmp {
		boolean compare(double value, double cutoff);
	}
	
	public static final BinCutoffCmp LT = new BinCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final BinCutoffCmp LE = new BinCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final BinCutoffCmp EQ = new BinCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value == cutoff; }
	};
	
	public static final BinCutoffCmp GT = new BinCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value > cutoff; }
	};
	
	public static final BinCutoffCmp GE = new BinCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value >= cutoff; }
	};
	
	protected double cutoff;
	protected BinCutoffCmp cmp;
	
	public BinCutoffFilter(double cutoff, BinCutoffCmp cmp) {
		this.cutoff = cutoff;
		this.cmp = cmp;
	}
	
	@Override
	public double parseValue(String str) {
		double value = Double.NaN;
		try {
			value = Double.parseDouble(str);
			value = cmp.compare(value, cutoff) ? 1 : 0;
		}
		catch (NumberFormatException e) {}
		return value;
	}

	@Override
	public double apply(double value) {
		return Double.isNaN(value) ? Double.NaN :
			cmp.compare(value, cutoff) ? 1 : 0;
	}

}

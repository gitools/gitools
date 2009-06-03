package es.imim.bg.ztools.datafilters;

import cern.colt.function.DoubleFunction;

public class BinaryCutoffFilter 
		implements ValueFilter, DoubleFunction {

	public interface BinaryCutoffCmp {
		boolean compare(double value, double cutoff);
	}
	
	public static final BinaryCutoffCmp LT = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final BinaryCutoffCmp LE = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final BinaryCutoffCmp EQ = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value == cutoff; }
	};
	
	public static final BinaryCutoffCmp NE = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value != cutoff; }
	};
	
	public static final BinaryCutoffCmp GT = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value > cutoff; }
	};
	
	public static final BinaryCutoffCmp GE = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value >= cutoff; }
	};
	
	public static final BinaryCutoffCmp ABS_LT = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final BinaryCutoffCmp ABS_LE = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final BinaryCutoffCmp ABS_EQ = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value == cutoff; }
	};
	
	public static final BinaryCutoffCmp ABS_NE = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value != cutoff; }
	};
	
	public static final BinaryCutoffCmp ABS_GT = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value > cutoff; }
	};
	
	public static final BinaryCutoffCmp ABS_GE = new BinaryCutoffCmp() {
		@Override public boolean compare(double value, double cutoff) {
			return value >= cutoff; }
	};
	
	protected double cutoff;
	protected BinaryCutoffCmp cmp;
	
	public BinaryCutoffFilter(double cutoff, BinaryCutoffCmp cmp) {
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

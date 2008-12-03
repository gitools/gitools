package es.imim.bg.ztools.datafilters;

public class BinCutoffFilter implements ValueFilter {

	protected interface BinCmp {
		boolean apply(double value, double cutoff);
	}
	
	public static final BinCmp LT = new BinCmp() {
		@Override public boolean apply(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final BinCmp LE = new BinCmp() {
		@Override public boolean apply(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final BinCmp EQ = new BinCmp() {
		@Override public boolean apply(double value, double cutoff) {
			return value == cutoff; }
	};
	
	public static final BinCmp GT = new BinCmp() {
		@Override public boolean apply(double value, double cutoff) {
			return value > cutoff; }
	};
	
	public static final BinCmp GE = new BinCmp() {
		@Override public boolean apply(double value, double cutoff) {
			return value >= cutoff; }
	};
	
	protected double cutoff;
	protected BinCmp cmp;
	
	public BinCutoffFilter(double cutoff, BinCmp cmp) {
		this.cutoff = cutoff;
		this.cmp = cmp;
	}
	
	@Override
	public double parseValue(String str) {
		double value = Double.NaN;
		try {
			value = Double.parseDouble(str);
			value = cmp.apply(value, cutoff) ? 1 : 0;
		}
		catch (NumberFormatException e) {}
		return value;
	}

}

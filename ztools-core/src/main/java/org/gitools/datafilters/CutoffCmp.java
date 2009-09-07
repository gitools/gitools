package org.gitools.datafilters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public abstract class CutoffCmp implements Serializable {

	private static final long serialVersionUID = 6256197968707025432L;

	public static final CutoffCmp LT = new CutoffCmp("<", "less than") {
		@Override public boolean compare(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final CutoffCmp LE = new CutoffCmp("<=", "less or equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final CutoffCmp EQ = new CutoffCmp("=", "equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value == cutoff; }
	};
	
	public static final CutoffCmp NE = new CutoffCmp("!=", "not equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value != cutoff; }
	};
	
	public static final CutoffCmp GT = new CutoffCmp(">", "greater than") {
		@Override public boolean compare(double value, double cutoff) {
			return value > cutoff; }
	};
	
	public static final CutoffCmp GE = new CutoffCmp(">=", "greater or equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value >= cutoff; }
	};
	
	public static final CutoffCmp ABS_LT = new CutoffCmp("abs <", "absolute less than") {
		@Override public boolean compare(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final CutoffCmp ABS_LE = new CutoffCmp("abs <=", "absolute less or equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final CutoffCmp ABS_EQ = new CutoffCmp("abs =", "absolute equal") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) == cutoff; }
	};
	
	public static final CutoffCmp ABS_NE = new CutoffCmp("abs !=", "absolute not equal") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) != cutoff; }
	};
	
	public static final CutoffCmp ABS_GT = new CutoffCmp("abs >", "absolute greater than") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) > cutoff; }
	};
	
	public static final CutoffCmp ABS_GE = new CutoffCmp("abs >=", "absolute greater or equal") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) >= cutoff; }
	};
	
	public static final CutoffCmp[] comparators = new CutoffCmp[] {
		LT, LE, GT, GE, EQ, NE,
		ABS_LT, ABS_LE, ABS_GT, ABS_GE, ABS_EQ, ABS_NE
	};
	
	public static final Map<String, CutoffCmp> abbreviatedNameMap = new HashMap<String, CutoffCmp>();
	public static final Map<String, CutoffCmp> shortNameMap = new HashMap<String, CutoffCmp>();
	public static final Map<String, CutoffCmp> longNameMap = new HashMap<String, CutoffCmp>();
	public static final Map<String, CutoffCmp> nameMap = new HashMap<String, CutoffCmp>();
	
	static {
		abbreviatedNameMap.put("lt", CutoffCmp.LT);
		abbreviatedNameMap.put("le", CutoffCmp.LE);
		abbreviatedNameMap.put("gt", CutoffCmp.GT);
		abbreviatedNameMap.put("ge", CutoffCmp.GE);
		abbreviatedNameMap.put("eq", CutoffCmp.EQ);
		abbreviatedNameMap.put("ne", CutoffCmp.NE);
		abbreviatedNameMap.put("alt", CutoffCmp.ABS_LT);
		abbreviatedNameMap.put("ale", CutoffCmp.ABS_LE);
		abbreviatedNameMap.put("agt", CutoffCmp.ABS_GT);
		abbreviatedNameMap.put("age", CutoffCmp.ABS_GE);
		abbreviatedNameMap.put("aeq", CutoffCmp.ABS_EQ);
		abbreviatedNameMap.put("ane", CutoffCmp.ABS_NE);
		
		for (CutoffCmp cmp : comparators) {
			shortNameMap.put(cmp.getShortName(), cmp);
			longNameMap.put(cmp.getLongName(), cmp);
			nameMap.put(cmp.getShortName(), cmp);
			nameMap.put(cmp.getLongName(), cmp);
		}
	}
	
	private String shortName;
	private String longName;

	public CutoffCmp(String shortName, String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public String getLongName() {
		return longName;
	}
	
	public abstract boolean compare(double value, double cutoff);
	
	@Override
	public String toString() {
		return shortName;
	}
}

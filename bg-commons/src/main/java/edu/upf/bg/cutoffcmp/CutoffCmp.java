package edu.upf.bg.cutoffcmp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public abstract class CutoffCmp implements Serializable {

	private static final long serialVersionUID = 6256197968707025432L;

	public static final CutoffCmp LT = new CutoffCmp("lt", "<", "less than") {
		@Override public boolean compare(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final CutoffCmp LE = new CutoffCmp("le", "<=", "less or equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final CutoffCmp EQ = new CutoffCmp("eq", "=", "equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value == cutoff; }
	};
	
	public static final CutoffCmp NE = new CutoffCmp("ne", "!=", "not equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value != cutoff; }
	};
	
	public static final CutoffCmp GT = new CutoffCmp("gt", ">", "greater than") {
		@Override public boolean compare(double value, double cutoff) {
			return value > cutoff; }
	};
	
	public static final CutoffCmp GE = new CutoffCmp("ge", ">=", "greater or equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value >= cutoff; }
	};
	
	public static final CutoffCmp ABS_LT = new CutoffCmp("alt", "abs <", "absolute less than") {
		@Override public boolean compare(double value, double cutoff) {
			return value < cutoff; }
	};
	
	public static final CutoffCmp ABS_LE = new CutoffCmp("ale", "abs <=", "absolute less or equal") {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final CutoffCmp ABS_EQ = new CutoffCmp("aeq", "abs =", "absolute equal") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) == cutoff; }
	};
	
	public static final CutoffCmp ABS_NE = new CutoffCmp("ane", "abs !=", "absolute not equal") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) != cutoff; }
	};
	
	public static final CutoffCmp ABS_GT = new CutoffCmp("agt", "abs >", "absolute greater than") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) > cutoff; }
	};
	
	public static final CutoffCmp ABS_GE = new CutoffCmp("age", "abs >=", "absolute greater or equal") {
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
		/*abbreviatedNameMap.put("lt", CutoffCmp.LT);
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
		abbreviatedNameMap.put("ane", CutoffCmp.ABS_NE);*/
		
		for (CutoffCmp cmp : comparators) {
			abbreviatedNameMap.put(cmp.getAbbreviation(), cmp);
			shortNameMap.put(cmp.getShortName(), cmp);
			longNameMap.put(cmp.getLongName(), cmp);
			nameMap.put(cmp.getAbbreviation(), cmp);
			nameMap.put(cmp.getShortName(), cmp);
			nameMap.put(cmp.getLongName(), cmp);
		}
	}

	public static CutoffCmp getFromName(String name) {
		/*CutoffCmp cmp = abbreviatedNameMap.get(name);
		if (cmp != null)
			return cmp;*/

		return nameMap.get(name);
	}

	private String abbreviation;
	private String shortName;
	private String longName;

	public CutoffCmp(String abbreviation, String shortName, String longName) {
		this.abbreviation = abbreviation;
		this.shortName = shortName;
		this.longName = longName;
	}

	public String getAbbreviation() {
		return abbreviation;
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

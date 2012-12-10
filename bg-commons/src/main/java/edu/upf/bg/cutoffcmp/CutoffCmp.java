/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package edu.upf.bg.cutoffcmp;

import org.apache.commons.collections.bidimap.DualHashBidiMap;

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
	
	public static final CutoffCmp LE = new CutoffCmp("le", "<=", "less than or equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return value <= cutoff; }
	};
	
	public static final CutoffCmp EQ = new CutoffCmp("eq", "=", "equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return value == cutoff; }
	};
	
	public static final CutoffCmp NE = new CutoffCmp("ne", "!=", "not equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return value != cutoff; }
	};
	
	public static final CutoffCmp GT = new CutoffCmp("gt", ">", "greater than") {
		@Override public boolean compare(double value, double cutoff) {
			return value > cutoff; }
	};
	
	public static final CutoffCmp GE = new CutoffCmp("ge", ">=", "greater than or equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return value >= cutoff; }
	};
	
	public static final CutoffCmp ABS_LT = new CutoffCmp("alt", "abs <", "absolute less than") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) < Math.abs(cutoff); }
	};
	
	public static final CutoffCmp ABS_LE = new CutoffCmp("ale", "abs <=", "absolute less than or equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) <= Math.abs(cutoff); }
	};
	
	public static final CutoffCmp ABS_EQ = new CutoffCmp("aeq", "abs =", "absolute equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) == Math.abs(cutoff); }
	};
	
	public static final CutoffCmp ABS_NE = new CutoffCmp("ane", "abs !=", "absolute not equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) != Math.abs(cutoff); }
	};
	
	public static final CutoffCmp ABS_GT = new CutoffCmp("agt", "abs >", "absolute greater than") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) > Math.abs(cutoff); }
	};
	
	public static final CutoffCmp ABS_GE = new CutoffCmp("age", "abs >=", "absolute greater than or equal to") {
		@Override public boolean compare(double value, double cutoff) {
			return Math.abs(value) >= Math.abs(cutoff); }
	};

	public static final CutoffCmp[] comparators = new CutoffCmp[] {
		LT, LE, GT, GE, EQ, NE,
		ABS_LT, ABS_LE, ABS_GT, ABS_GE, ABS_EQ, ABS_NE
	};
    
    private static final DualHashBidiMap opposite = new DualHashBidiMap();
    static {
        opposite.put(CutoffCmp.LT, CutoffCmp.GE);
        opposite.put(CutoffCmp.LE, CutoffCmp.GT);
        opposite.put(CutoffCmp.EQ, CutoffCmp.NE);
        opposite.put(CutoffCmp.ABS_LT, CutoffCmp.ABS_GE);
        opposite.put(CutoffCmp.ABS_LE, CutoffCmp.ABS_GT);
        opposite.put(CutoffCmp.ABS_EQ, CutoffCmp.ABS_NE);
    }
	
	public static final Map<String, CutoffCmp> abbreviatedNameMap = new HashMap<String, CutoffCmp>();
	public static final Map<String, CutoffCmp> shortNameMap = new HashMap<String, CutoffCmp>();
	public static final Map<String, CutoffCmp> longNameMap = new HashMap<String, CutoffCmp>();
	public static final Map<String, CutoffCmp> nameMap = new HashMap<String, CutoffCmp>();
	
	static {		
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
    
    public static CutoffCmp getOpposite(CutoffCmp key) {
        if (opposite.get(key) == null) 
            return (CutoffCmp) (opposite.getKey(key));
        else 
            return (CutoffCmp) (opposite.get(key));
    }
	
	public abstract boolean compare(double value, double cutoff);
	
	@Override
	public String toString() {
		return shortName;
	}
}

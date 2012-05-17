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

package edu.upf.bg.operators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public abstract class Operator implements Serializable {

	private static final long serialVersionUID = 6256197968707025432L;

	public static final Operator AND = new Operator("and", "&", "and") {
                 @Override
		public boolean evaluate(boolean bool1, boolean bool2) {
			return bool1 && bool2;
                 }
	};

       public static final Operator OR = new Operator("or", "|", "or") {
                 @Override
		public boolean evaluate(boolean bool1, boolean bool2) {
			return bool1 || bool2;
                 }
	};

        public static final Operator EMPTY = new Operator("", "", "") {
                @Override
		public boolean evaluate(boolean bool1, boolean bool2) {
			return true;
                 }
	};

	public static final Operator[] operators = new Operator[] {
		AND, OR, EMPTY
	};
	
	public static final Map<String, Operator> abbreviatedNameMap = new HashMap<String, Operator>();
	public static final Map<String, Operator> shortNameMap = new HashMap<String, Operator>();
	public static final Map<String, Operator> longNameMap = new HashMap<String, Operator>();
	public static final Map<String, Operator> nameMap = new HashMap<String, Operator>();
	
	static {		
		for (Operator op : operators) {
			abbreviatedNameMap.put(op.getAbbreviation(), op);
			shortNameMap.put(op.getShortName(), op);
			longNameMap.put(op.getLongName(), op);
			nameMap.put(op.getAbbreviation(), op);
			nameMap.put(op.getShortName(), op);
			nameMap.put(op.getLongName(), op);
		}
	}

	public static Operator getFromName(String name) {
		return nameMap.get(name);
	}

	private String abbreviation;
	private String shortName;
	private String longName;

	public Operator(String abbreviation, String shortName, String longName) {
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
	
	public abstract boolean evaluate(boolean bool1, boolean bool2);
	
	@Override
	public String toString() {
		return shortName;
	}
}

/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.utils.operators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    private static final Operator[] operators = new Operator[]{AND, OR, EMPTY};

    private static final Map<String, Operator> abbreviatedNameMap = new HashMap<String, Operator>();
    private static final Map<String, Operator> shortNameMap = new HashMap<String, Operator>();
    private static final Map<String, Operator> longNameMap = new HashMap<String, Operator>();
    private static final Map<String, Operator> nameMap = new HashMap<String, Operator>();

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

    private final String abbreviation;
    private final String shortName;
    private final String longName;

    public Operator(String abbreviation, String shortName, String longName) {
        this.abbreviation = abbreviation;
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    String getShortName() {
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

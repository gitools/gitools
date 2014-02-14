/*
 * #%L
 * org.gitools.utils
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.utils.text;

import java.util.Map;


public enum Separator {

    TAB("Tab", '\t'),
    COMMA("Comma", ','),
    SEMICOLON("Semicolon", ';');

    private String name;
    private char separatorChar;
    private static final Map<String, Separator> nameMap = null;
    private static final Map<Character, Separator> charMap = null;

    private static void initMapping() {
        for (Separator sep : values()) {
            nameMap.put(sep.getName(), sep);
            charMap.put(sep.getChar(), sep);
        }
    }

    private Separator(String name, char separatorChar) {

        this.name = name;
        this.separatorChar = separatorChar;
    }


    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public static Separator fromName(String name) {
        if (nameMap == null) {
            initMapping();
        }
        return nameMap.get(name);
    }

    public static Separator fromChar(Character separatorCharchar) {
        if (charMap == null) {
            initMapping();
        }
        return charMap.get(separatorCharchar);
    }

    public char getChar() {
        return separatorChar;
    }
}

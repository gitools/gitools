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

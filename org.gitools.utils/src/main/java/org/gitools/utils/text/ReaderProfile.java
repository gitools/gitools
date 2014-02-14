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


public abstract class ReaderProfile {

    public static String TABLE = "Table";
    public static String MATRIX = "Matrix";
    public static String[] FILE_LAYOUTS = new String[]{MATRIX, TABLE};

    String name;
    Separator separator;
    String layout;
    int skipLines;
    char commentChar;
    String metaDataChar = "#?";
    int[] ignoredColumns;


    protected ReaderProfile() {
        this.name = "default";
        this.separator = Separator.TAB;
        this.skipLines = 0;
        this.commentChar = '#';
        this.ignoredColumns = new int[0];
    }

    public static ReaderProfile fromProfile(ReaderProfile profile) {
        return profile;
    }

    public char getCommentChar() {
        return commentChar;
    }

    public void setCommentChar(char commentChar) {
        this.commentChar = commentChar;
    }


    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getSkipLines() {
        return skipLines;
    }

    public void setSkipLines(int skipLines) {
        this.skipLines = skipLines;
    }

    public String getMetaDataChar() {
        return metaDataChar;
    }

    public int[] getIgnoredColumns() {
        return ignoredColumns;
    }

    public void setIgnoredColumns(int[] ignoredColumns) {
        this.ignoredColumns = ignoredColumns;
    }
}

/*
 * #%L
 * gitools-obo
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
package org.gitools.obo;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * @noinspection ALL
 */
public class OBOEvent implements OBOEventTypes {

    private final int type;
    private String stanzaName;
    private String tagName;
    private String tagContents;

    private final int linePos;

    public OBOEvent(int type, int line) {
        this.type = type;
        this.linePos = line;
    }

    public OBOEvent(int type, int line, String stanzaName) {
        this.type = type;
        this.linePos = line;
        this.stanzaName = stanzaName;
    }

    public OBOEvent(int type, int line, String stanzaName, String tagName) {
        this.type = type;
        this.linePos = line;
        this.stanzaName = stanzaName;
        this.tagName = tagName;
    }

    public OBOEvent(int type, int line, String stanzaName, String tagName, String tagContents) {
        this.type = type;
        this.linePos = line;
        this.stanzaName = stanzaName;
        this.tagName = tagName;
        this.tagContents = tagContents;
    }

    public int getType() {
        return type;
    }

    public int getLinePos() {
        return linePos;
    }

    public String getStanzaName() {
        return stanzaName;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagContents() {
        return tagContents;
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        try {
            for (Field f : OBOEventTypes.class.getFields()) {
                if (f.getInt(null) == type) {
                    sb.append(f.getName());
                    break;
                }
            }
        } catch (Exception ex) {
        }

        if (sb.length() == 0) {
            sb.append(type);
        }

        sb.append(" ");

        switch (type) {
            case STANZA_START:
                sb.append(stanzaName);
                break;
            case TAG_START:
                sb.append(tagName);
                break;
        }

        return sb.toString();
    }
}

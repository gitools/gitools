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
package org.gitools.datasources.obo;

/**
 * @noinspection ALL
 */
public interface OBOEventTypes {

    static final int UNKNOWN = -1;
    static final int COMMENT = 1;
    static final int DOCUMENT_START = 10;
    static final int DOCUMENT_END = 19;
    static final int HEADER_START = 20;
    static final int HEADER_END = 29;
    static final int STANZA_START = 30;
    static final int STANZA_END = 39;
    static final int TAG_START = 40;
    static final int TAG_END = 49;
}

/*
 * #%L
 * gitools-ui-app
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
package org.gitools.utils.readers;

import java.io.Serializable;

public class FileHeader implements Serializable {

    protected final int pos;
    protected final String label;

    public FileHeader(String label, int pos) {
        this.label = label;
        this.pos = pos;
    }

    public String getLabel() {
        return label;
    }

    public int getPos() {
        return pos;
    }


    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileHeader that = (FileHeader) o;

        if (pos != that.pos) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return pos;
    }
}

/*
 * #%L
 * org.gitools.matrix
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
package org.gitools.matrix.model.mtabixmatrix;

import gnu.trove.map.TLongDoubleMap;
import gnu.trove.map.hash.TLongDoubleHashMap;

public class MTabixBlockValues {

    private TLongDoubleMap map;

    public MTabixBlockValues() {
        super();
        this.map = new TLongDoubleHashMap();
    }

    public Double get(String... identifiers) {
        return map.get(hash(identifiers));
    }

    public void set(Double value, String row, String column) {

        if (value == null) {
            return;
        }

        map.put(hash(row, column), value);
    }

    private long hash(String... identifiers) {
        int a = identifiers[0].hashCode();
        int b = identifiers[1].hashCode();
        return (long) a << 32 | b & 0xFFFFFFFFL;
    }
}

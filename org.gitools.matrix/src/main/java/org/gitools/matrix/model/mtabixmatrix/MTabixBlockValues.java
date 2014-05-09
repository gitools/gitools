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

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;
import java.util.Map;

public class MTabixBlockValues {
    private TIntObjectMap<TIntDoubleMap> map;
    public MTabixBlockValues() {
        super();
        this.map = new TIntObjectHashMap<>();
    }

    public Double get(String... identifiers) {
        TIntDoubleMap submap = map.get(identifiers[0].hashCode());
        return submap==null ? null : submap.get(identifiers[1].hashCode());
    }

    public void set(Double value, String row, String column) {

        if (value == null) {
            return;
        }

        if (!map.containsKey(row.hashCode())) {
            map.put(row.hashCode(), new TIntDoubleHashMap());
        }
        map.get(row.hashCode()).put(column.hashCode(), value);
    }
}

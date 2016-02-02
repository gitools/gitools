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

import edu.upf.bg.mtabix.parse.IKeyParser;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.map.hash.TLongDoubleHashMap;

import java.util.Map;

public class MTabixBlockValues {

    private IKeyParser keyParser;
    private Map<Integer, Map<String, Integer>> identifierToPosition;
    private TLongDoubleMap map;

    public MTabixBlockValues(IKeyParser keyParser, Map<Integer, Map<String, Integer>> identifierToPosition) {
        super();

        this.map = new TLongDoubleHashMap();
        this.keyParser = keyParser;
        this.identifierToPosition = identifierToPosition;
    }

    public Double get(String... identifiers) {

        long hash = hash(identifiers);

        if (!map.containsKey(hash)) {
            return null;
        }

        return map.get(hash);
    }

    public void put(long key, double value) {
        map.put(key, value);
    }

    public long hash(String... identifiers) {
        int a = identifierToPosition.get(keyParser.getKeys()[0]).get(identifiers[0]);
        int b = identifierToPosition.get(keyParser.getKeys()[1]).get(identifiers[1]);
        return (long) a << 32 | b & 0xFFFFFFFFL;
    }
}

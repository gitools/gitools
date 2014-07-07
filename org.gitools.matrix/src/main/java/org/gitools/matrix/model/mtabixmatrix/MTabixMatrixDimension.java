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

import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;

import java.util.*;

public class MTabixMatrixDimension extends HashMatrixDimension {

    private Comparator<String> comparator;

    public MTabixMatrixDimension(MatrixDimensionKey id, List<String> labels) {
        super(id, labels);
        this.comparator = new MTabixComparator(labels);
    }

    @Override
    public IMatrixDimension subset(Set<String> identifiers) {
        List<String> ids = new ArrayList<>(identifiers);
        Collections.sort(ids, comparator);
        return new HashMatrixDimension(getId(), ids);
    }

    private static class MTabixComparator implements Comparator<String> {

        private Map<String, Integer> order;

        private MTabixComparator(List<String> labels) {

            order = new HashMap<>(labels.size());

            for (int i=0; i < labels.size(); i++) {
                order.put(labels.get(i), Integer.valueOf(i));
            }
        }

        @Override
        public int compare(String o1, String o2) {

            Integer i1 = order.get(o1);
            Integer i2 = order.get(o2);

            if (i1 == null && i2 == null) {
                return 0;
            }

            if (i1 == null) {
                return 1;
            }

            if (i2 == null) {
                return -1;
            }

            return i1.compareTo(i2);
        }
    }
}

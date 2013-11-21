/*
 * #%L
 * gitools-core
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
package org.gitools.core.matrix.model.iterable;

import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.position.IMatrixPosition;
import org.gitools.api.matrix.position.IMatrixPositionMapping;

import java.util.HashMap;
import java.util.Map;

public class PositionMapping implements IMatrixPositionMapping {

    private Map<MatrixDimensionKey, MatrixDimensionKey> mappings = new HashMap<>();
    private Map<MatrixDimensionKey, String> fixed = new HashMap<>();

    @Override
    public void map(IMatrixPosition from, IMatrixPosition to) {

        for (MatrixDimensionKey key : to.getMatrix().getDimensionKeys()) {

            if (fixed.containsKey(key)) {
                to.set(key, fixed.get(key));

            } else if (mappings.containsKey(key)) {
                to.set(key, from.get(mappings.get(key)));

            } else {
                to.set(from.get(key));
            }

        }
    }

    public PositionMapping map(IMatrixDimension from, IMatrixDimension to) {
        return map(from.getId(), to.getId());
    }

    public PositionMapping map(MatrixDimensionKey from, IMatrixDimension to) {
        return map(from, to.getId());
    }

    public PositionMapping map(IMatrixDimension from, MatrixDimensionKey to) {
        return map(from.getId(), to);
    }

    public PositionMapping map(MatrixDimensionKey from, MatrixDimensionKey to) {
        mappings.put(to, from);
        return this;
    }

    public PositionMapping fix(IMatrixDimension dimension, String identifier) {
        return fix(dimension.getId(), identifier);
    }

    public PositionMapping fix(MatrixDimensionKey dimensionKey, String identifier) {
        fixed.put(dimensionKey, identifier);
        return this;
    }


}

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
package org.gitools.matrix.model.hashmatrix;

import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.MatrixLayers;

import java.util.List;

public class TriangularHashMatrix extends HashMatrix {

    List<String> idOrder;

    public TriangularHashMatrix(MatrixLayers layers, MatrixDimensionKey... dimensions) {
        super(layers, dimensions);
    }

    public TriangularHashMatrix(MatrixLayers<? extends IMatrixLayer> layers, IMatrixDimension... dimensions) {
        super(layers, dimensions);
    }

    public TriangularHashMatrix(MatrixLayers<? extends IMatrixLayer> layers, HashMatrixDimension... dimensions) {
        super(layers, dimensions);
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {
        return super.get(layer, doInvert(identifiers));
    }

    private String[] doInvert(String[] identifiers) {
        if (getDimension(MatrixDimensionKey.COLUMNS).indexOf(identifiers[0]) >=
                getDimension(MatrixDimensionKey.ROWS).indexOf(identifiers[1])) {
            return new String[]{identifiers[1], identifiers[0]};
        } else {
            return identifiers;
        }
    }

}

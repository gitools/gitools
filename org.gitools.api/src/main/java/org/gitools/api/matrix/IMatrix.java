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
package org.gitools.api.matrix;

import org.gitools.api.resource.IResource;

public interface IMatrix extends IResource {

    MatrixDimensionKey[] getDimensionKeys();

    IMatrixDimension getDimension(MatrixDimensionKey dimension);

    IMatrixLayers<? extends IMatrixLayer> getLayers();

    <T> T get(IMatrixLayer<T> layer, IMatrixPosition position);

    <T> T get(IMatrixLayer<T> layer, String... identifiers);

    <T> void set(IMatrixLayer<T> layer, T value, IMatrixPosition position);

    <T> void set(IMatrixLayer<T> layer, T value, String... identifiers);

    IMatrixPosition newPosition();

    IMatrix subset(IMatrixDimension... dimensionSubsets);

    /**
     * This method is called when the matrix is not in use.
     * <p/>
     * It's a good practice to free all the caching memory usage.
     * @param topLayer
     */
    void detach(IMatrixLayer topLayer);

    <T> void setMetadata(IKey<T> key, T value);

    <T> T getMetadata(IKey<T> key);

    IMatrixDimension getRows();

    IMatrixDimension getColumns();

}

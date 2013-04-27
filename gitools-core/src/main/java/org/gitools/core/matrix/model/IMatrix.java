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
package org.gitools.core.matrix.model;

import org.gitools.core.persistence.IResource;

public interface IMatrix extends IResource {

    IMatrixDimension getRows();

    IMatrixDimension getColumns();

    IMatrixLayers getLayers();

    @Deprecated
    Object getValue(int row, int column, int layer);

    Object getValue(int[] position, int layer);

    @Deprecated
    void setValue(int row, int column, int layer, Object value);

    void setValue(int[] position, int layer, Object value);

    /**
     * This method is called when the matrix is not in use.
     * <p/>
     * It's a good practice to free all the caching memory usage.
     */
    void detach();

}
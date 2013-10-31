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
package org.gitools.core.matrix.model.matrix.element;

import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;

import java.io.Serializable;

public abstract class ElementAdapter<T extends MatrixLayer> implements Serializable {

    private Class<?> elementClass;

    private MatrixLayers<T> matrixLayers;

    protected ElementAdapter(Class<?> elementClass) {
        this.elementClass = elementClass;
    }

    void setElementClass(Class<?> elementClass) {
        this.elementClass = elementClass;
    }

    public Class<?> getElementClass() {
        return elementClass;
    }

    public final MatrixLayers<T> getMatrixLayers() {
        return matrixLayers;
    }

    final void setMatrixLayers(MatrixLayers<T> matrixLayers) {
        this.matrixLayers = matrixLayers;
    }

    public abstract void setCell(IMatrix resultsMatrix, int row, int column, Object result);

    public abstract Object getCell(IMatrix resultsMatrix, int row, int column);
}
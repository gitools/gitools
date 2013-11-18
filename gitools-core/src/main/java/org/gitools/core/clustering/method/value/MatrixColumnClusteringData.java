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
package org.gitools.core.clustering.method.value;

import org.gitools.core.clustering.ClusteringData;
import org.gitools.core.clustering.ClusteringDataInstance;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.jetbrains.annotations.NotNull;

public class MatrixColumnClusteringData implements ClusteringData {

    public class Instance implements ClusteringDataInstance {

        private final int index;

        public Instance(int index) {
            this.index = index;
        }

        @Override
        public int getNumAttributes() {
            return matrix.getRows().size();
        }

        @Override
        public String getAttributeName(int attribute) {
            return matrix.getRows().getLabel(attribute);
        }

        @Override
        public Class<?> getValueClass(int attribute) {
            return layer.getValueClass();
        }

        @Override
        public Object getValue(int attribute) {
            return matrix.get(layer, matrix.getRows().getLabel(attribute), matrix.getColumns().getLabel(index));
        }

        @NotNull
        @Override
        public <T> T getTypedValue(int attribute, Class<T> valueClass) {
            if (!valueClass.equals(getValueClass(attribute))) {
                throw new RuntimeException("Unsupported type: " + valueClass.getCanonicalName());
            }

            return (T) matrix.get(layer, matrix.getRows().getLabel(attribute), matrix.getColumns().getLabel(index));
        }
    }

    private IMatrix matrix;
    private IMatrixLayer layer;

    public MatrixColumnClusteringData(IMatrix matrix, IMatrixLayer layer) {
        this.matrix = matrix;
        this.layer = layer;
    }

    @Override
    public int getSize() {
        return matrix.getColumns().size();
    }

    @Override
    public String getLabel(int index) {
        return matrix.getColumns().getLabel(index);
    }

    @Override
    public ClusteringDataInstance getInstance(int index) {
        return new Instance(index);
    }

    public IMatrix getMatrix() {
        return matrix;
    }

    public void setMatrix(IMatrix matrix) {
        this.matrix = matrix;
    }

    public int getNumAttributes() {
        return matrix.getRows().size();
    }

}

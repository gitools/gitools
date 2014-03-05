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
package org.gitools.analysis.clustering.method.value;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringDataInstance;
import org.gitools.api.matrix.*;

public class MatrixClusteringData implements ClusteringData {

    private IMatrix matrix;
    private IMatrixDimension clusteringDimension;
    private IMatrixDimension aggregationDimension;
    private IMatrixLayer layer;

    public IMatrixDimension getClusteringDimension() {
        return clusteringDimension;
    }

    public IMatrixDimension getAggregationDimension() {
        return aggregationDimension;
    }

    public IMatrixLayer getLayer() {
        return layer;
    }

    public MatrixClusteringData(IMatrix matrix, MatrixDimensionKey clusteringDimensionKey, MatrixDimensionKey aggregationDimensionKey, String layerId) {
        this.matrix = matrix;
        this.clusteringDimension = matrix.getDimension(clusteringDimensionKey);
        this.aggregationDimension = matrix.getDimension(aggregationDimensionKey);
        this.layer = matrix.getLayers().get(layerId);
    }

    @Override
    public int getSize() {
        return clusteringDimension.size();
    }

    @Override
    public String getLabel(int index) {
        return clusteringDimension.getLabel(index);
    }

    @Override
    public Iterable<String> getLabels() {
        return clusteringDimension;
    }

    @Override
    public ClusteringDataInstance getInstance(int index) {
        return getInstance(clusteringDimension.getLabel(index));
    }

    @Override
    public ClusteringDataInstance getInstance(String label) {
        return new Instance(label);
    }

    public IMatrix getMatrix() {
        return matrix;
    }

    public void setMatrix(IMatrix matrix) {
        this.matrix = matrix;
    }

    public int getNumAttributes() {
        return aggregationDimension.size();
    }

    private class Instance implements ClusteringDataInstance {

        private final IMatrixPosition position;

        public Instance(String label) {
            this.position = matrix.newPosition().set(clusteringDimension, label);
        }

        @Override
        public int getNumAttributes() {
            return aggregationDimension.size();
        }

        @Override
        public String getAttributeName(int attribute) {
            return aggregationDimension.getLabel(attribute);
        }

        @Override
        public Class<?> getValueClass(int attribute) {
            return layer.getValueClass();
        }

        @Override
        public Object getValue(int attribute) {
            return matrix.get(layer, position.set(aggregationDimension, aggregationDimension.getLabel(attribute)));
        }

        @Override
        public <T> T getTypedValue(int attribute, Class<T> valueClass) {
            if (!valueClass.equals(getValueClass(attribute))) {
                throw new RuntimeException("Unsupported type: " + valueClass.getCanonicalName());
            }

            return (T) getValue(attribute);
        }
    }

}

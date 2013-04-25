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
package org.gitools.core.heatmap.header;

import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.matrix.model.AbstractMatrix;
import org.gitools.core.matrix.model.IAnnotations;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixLayers;

import java.util.List;

class MatrixAdapter extends AbstractMatrix {

    private HeatmapDecoratorHeader header;

    public MatrixAdapter(HeatmapDecoratorHeader header) {
        this.header = header;
    }

    @Override
    public Object getValue(int[] position, int layerIndex) {

        if (layerIndex == -1) {
            return null;
        }

        HeatmapDimension heatmapDimension = header.getHeatmapDimension();
        IAnnotations annotations = heatmapDimension.getAnnotations();
        String identifier = heatmapDimension.getLabel(position[0]);
        String value = annotations.getAnnotation(identifier, getLabels().get(layerIndex));

        Double number;
        try {
            number = (value == null) ? null : Double.valueOf(value);
        } catch (Exception e) {
            number = null;
        }

        return number;
    }

    public int indexOf(String label) {
        return getLabels().indexOf(label);
    }

    private List<String> getLabels() {
        return header.getHeatmapDimension().getAnnotations().getLabels();
    }

    @Override
    public IMatrixDimension getRows() {
        return null;
    }

    @Override
    public IMatrixDimension getColumns() {
        return null;
    }

    @Override
    public void setValue(int[] position, int layerIndex, Object value) {
    }

    @Override
    public IMatrixLayers getLayers() {
        return null;
    }
}

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
package org.gitools.core.matrix.model.compressmatrix;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.core.matrix.model.AbstractMatrixDimension;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents one dimension (rows or columns) of a {@link CompressMatrix}.
 */
public class CompressDimension extends AbstractMatrixDimension {

    private String[] labels;
    private Map<String, Integer> labelsToIndex;

    /**
     * Instantiates a new Compress dimension.
     *
     * @param labels the identifier labels of this dimension
     */
    public CompressDimension(MatrixDimensionKey id, String[] labels) {
        super(id);

        this.labels = labels;
        this.labelsToIndex = new HashMap<>(labels.length);

        for (int i = 0; i < labels.length; i++) {
            this.labelsToIndex.put(labels[i], i);
        }
    }

    /**
     * Total number of labels in this dimension.
     *
     * @return the int
     */
    @Override
    public int size() {
        return labels.length;
    }

    /**
     * The identifier label at 'index' position.
     *
     * @param index the index
     * @return the label
     */
    @Override
    public String getLabel(int index) {
        return labels[index];
    }

    /**
     * The position of the 'label' item.
     *
     * @param label the label
     * @return the index
     */
    @Override
    public int indexOf(String label) {
        Integer value = labelsToIndex.get(label);

        if (value == null) {
            return -1;
        }

        return value.intValue();
    }

    /**
     * Get all the labels.
     *
     * @return the string array
     */
    public String[] getLabels() {
        return labels;
    }

}

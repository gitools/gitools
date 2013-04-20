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
package org.gitools.matrix.model;

public interface IMatrixDimension {

    /**
     * Gets the dimension identifier.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets the index in the matrix position vector of this dimension
     *
     * @return the vector position
     */
    int getVectorPosition();

    /**
     * Total number of items in this dimension
     *
     * @return the dimension count
     */
    int size();


    /**
     * Sets index in the matrix position.vector for this dimension
     *
     * @param matrixPosition the matrix position
     * @param index the index
     * @return the position
     */
    void setPosition(int[] matrixPosition, int index);

    int getPosition(int[] matrixPosition);


    /**
     * Increments the index of this dimension in the matrix position. If we are in the
     * last position it will set it to zero and return -1. Otherwise it returns the new
     * position index.
     *
     * @param matrixPosition the matrix position
     * @return The next index or -1 if there are no more index.
     */
    int next(int[] matrixPosition);

    /**
     * Gets the identifier label at the given position
     *
     * @param index the index
     * @return the label
     */
    String getLabel(int index);

    /**
     * Gets the index position of this identifier
     *
     * @param label the label
     * @return the index
     */
    int getIndex(String label);

}

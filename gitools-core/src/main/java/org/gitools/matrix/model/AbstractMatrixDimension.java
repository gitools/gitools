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

import com.jgoodies.binding.beans.Model;

public abstract class AbstractMatrixDimension extends Model implements IMatrixDimension {

    private int vectorPosition;
    private String id;

    protected AbstractMatrixDimension() {
        super();
    }

    public AbstractMatrixDimension(String id, int vectorPosition) {
        super();

        this.id = id;
        this.vectorPosition = vectorPosition;
    }

    @Override
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Override
    public int getVectorPosition() {
        return vectorPosition;
    }

    protected void setVectorPosition(int vectorPosition) {
        this.vectorPosition = vectorPosition;
    }

    @Override
    public int getPosition(int[] matrixPosition) {
        return matrixPosition[vectorPosition];
    }

    @Override
    public void setPosition(int[] matrixPosition, int index) {
        matrixPosition[vectorPosition] = index;
    }

    @Override
    public int next(int[] matrixPosition) {
        int next = matrixPosition[vectorPosition]++;

        if (next == size()) {
            matrixPosition[vectorPosition] = 0;
            next = -1;
        } else {
            matrixPosition[vectorPosition] = next;
        }

        return next;
    }
}

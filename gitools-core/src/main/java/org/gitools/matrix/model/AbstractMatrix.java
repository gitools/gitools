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

import org.gitools.model.Resource;

public abstract class AbstractMatrix extends Resource implements IMatrix {

    @Override
    public Object getValue(int row, int column, int layerIndex) {
        return getValue(new int[]{ row, column }, layerIndex);
    }

    @Override
    public void setValue(int row, int column, int layer, Object value) {
        setValue(new int[]{row, column}, layer, value);
    }

    @Override
    public void detach() {
    }

}

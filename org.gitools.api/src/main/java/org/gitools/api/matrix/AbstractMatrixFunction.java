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

import org.gitools.api.matrix.IMatrixFunction;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixPosition;

public abstract class AbstractMatrixFunction<F, T> implements IMatrixFunction<F, T> {

    @Override
    public void onAfterIterate(IMatrixIterable<T> parentIterable) {
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<T> parentIterable) {
    }

    @Override
    public abstract F apply(T value, IMatrixPosition position);
}

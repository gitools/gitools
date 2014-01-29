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
package org.gitools.analysis._DEPRECATED.matrix.model.iterable;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.position.IMatrixIterable;
import org.gitools.api.matrix.position.IMatrixPosition;

import java.util.Iterator;

public abstract class AbstractChainIterable<F, T> extends AbstractIterable<F> {

    private static String ERROR_MESSAGE = "You must limit the iterable before operate on it.";

    private IMatrixIterable<T> parentIterable;

    protected AbstractChainIterable(IMatrixIterable<T> parentIterable) {
        this.parentIterable = parentIterable;
    }

    protected IMatrixIterable<T> getParentIterable() {
        return parentIterable;
    }

    protected Iterator<T> newParentIterator() {
        return parentIterable.iterator();
    }

    @Override
    public int size() {
        return parentIterable.size();
    }

    @Override
    public IMatrixDimension getIterateDimension() {
        return parentIterable.getIterateDimension();
    }

    @Override
    public IMatrixPosition getPosition() {
        return parentIterable.getPosition();
    }

    public IMatrix getMatrix() {
        return getPosition().getMatrix();
    }

    @Override
    public IMatrixIterable<F> from(String fromIdentifier) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public IMatrixIterable<F> to(String toIdentifier) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public IMatrixIterable<F> between(String fromIdentifier, String toIdentifier) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }
}

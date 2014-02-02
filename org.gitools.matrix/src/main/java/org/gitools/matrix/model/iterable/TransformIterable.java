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
package org.gitools.matrix.model.iterable;

import org.gitools.api.matrix.IMatrixFunction;

import java.util.Iterator;

public class TransformIterable<F, T> extends AbstractChainIterable<F, T> {

    private IMatrixFunction<F, T> function;

    public TransformIterable(AbstractIterable<T> parentIterable, IMatrixFunction<F, T> function) {
        super(parentIterable);

        this.function = function;
    }

    @Override
    public Iterator<F> iterator() {
        function.onBeforeIterate(getParentIterable());
        return new TransformIterator(newParentIterator());
    }

    private class TransformIterator extends AbstractChainIterator<F, T> {

        public TransformIterator(Iterator<T> parentIterator) {
            super(parentIterator);
        }

        @Override
        public boolean hasNext() {

            if (!super.hasNext()) {
                function.onAfterIterate(getParentIterable());
                return false;
            }

            return true;
        }

        @Override
        public F next() {
            return function.apply(parentNext(), getPosition());
        }
    }
}

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
package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.matrix.element.LayerAdapter;

import java.util.Iterator;

public class AdaptIterable<F> extends AbstractChainIterable<F, String> {

    private LayerAdapter<F> layerAdapter;

    public AdaptIterable(AbstractIterable<String> parentIterable, LayerAdapter<F> layerAdapter) {
        super(parentIterable);

        this.layerAdapter = layerAdapter;
    }

    @Override
    public Iterator<F> iterator() {
        return new AdaptIterator(newParentIterator());
    }

    private class AdaptIterator extends AbstractChainIterator<F, String> {

        public AdaptIterator(Iterator<String> parentIterator) {
            super(parentIterator);
        }

        @Override
        public F next() {

            parentNext();

            return layerAdapter.get(getMatrix(), getPosition());
        }
    }
}

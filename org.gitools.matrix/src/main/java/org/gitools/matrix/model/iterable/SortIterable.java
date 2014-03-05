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

import java.util.*;

public class SortIterable<T> extends AbstractChainIterable<T, T> {

    private Comparator<T> comparator;
    private List<Value> values;

    public SortIterable(AbstractIterable<T> parentIterable, Comparator<T> comparator) {
        super(parentIterable);

        this.comparator = comparator;
    }

    @Override
    public Iterator<T> iterator() {

        values = new ArrayList<>();

        for (T value : getParentIterable()) {
            String identifier = getPosition().get(getIterateDimension());
            values.add(new Value(identifier, value));
        }

        Collections.sort(values, new ValueComparator());

        return new SortIterator(values.iterator());
    }

    private class SortIterator extends AbstractChainIterator<T, Value> {
        public SortIterator(Iterator<Value> parentIterator) {
            super(parentIterator);
        }

        @Override
        public T next() {
            Value value = parentNext();
            getPosition().set(getIterateDimension(), value.identifier);
            return value.value;
        }
    }

    private class Value {
        private String identifier;
        private T value;

        private Value(String identifier, T value) {
            this.identifier = identifier;
            this.value = value;
        }
    }

    private class ValueComparator implements Comparator<Value> {
        @Override
        public int compare(Value o1, Value o2) {
            return comparator.compare(o1.value, o2.value);
        }
    }
}

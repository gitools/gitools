package org.gitools.core.matrix.model.iterable;

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

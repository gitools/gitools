package org.gitools.core.label;

import java.util.Iterator;

public abstract class AbstractLabelProvider implements LabelProvider {

    @Override
    public Iterator<String> iterator() {
        return new LabelIterator();
    }

    private class LabelIterator implements Iterator<String> {

        private int pos = 0;

        @Override
        public boolean hasNext() {
            return pos < getCount();
        }

        @Override
        public String next() {
            return getLabel(++pos);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }
}

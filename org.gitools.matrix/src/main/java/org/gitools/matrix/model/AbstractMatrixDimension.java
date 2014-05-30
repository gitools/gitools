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
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractMatrixDimension extends Model implements IMatrixDimension {

    private MatrixDimensionKey id;

    protected AbstractMatrixDimension() {
        super();
    }

    public AbstractMatrixDimension(MatrixDimensionKey id) {
        super();

        this.id = id;
    }

    @Override
    public MatrixDimensionKey getId() {
        return id;
    }

    protected void setId(MatrixDimensionKey id) {
        this.id = id;
    }

    @Override
    public boolean contains(String identifier) {
        return indexOf(identifier) != -1;
    }

    @Override
    public IMatrixDimension subset(Set<String> identifiers) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " dimension don't support subsetting");
    }

    @Override
    @Deprecated
    public IMatrixDimension from(String fromIdentifier) {

            int from = fromIdentifier == null ? 0 : indexOf(fromIdentifier);
            int to = size() - 1;

            Set<String> identifiers = new LinkedHashSet<>(to - from + 1);

            for (int i = from; i <= to; i++) {
                identifiers.add(getLabel(i));
            }

            return subset(identifiers);
    }

    @Override
    public Iterator<String> iterator() {
        return new MatrixDimensionIterator();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    private class MatrixDimensionIterator implements Iterator<String> {

        private int nextPosition;

        private MatrixDimensionIterator() {
            nextPosition = 0;
        }

        @Override
        public boolean hasNext() {
            return nextPosition < AbstractMatrixDimension.this.size();
        }

        @Override
        public String next() {
            return AbstractMatrixDimension.this.getLabel(nextPosition++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    @Override
    public String toString() {
        return "(dimension:" + id + " #" + size() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IMatrixDimension)) return false;

        IMatrixDimension strings = (IMatrixDimension) o;

        return id.equals(strings.getId());

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

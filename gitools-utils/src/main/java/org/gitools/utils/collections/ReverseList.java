/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.collections;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReverseList<T> extends AbstractList<T> {

    private List<T> list;

    public ReverseList(List<T> list) {
        this.list = list;
    }

    private int reverseIndex(int index) {
        return size() - index - 1;
    }

    @Override
    public T get(int index) {
        return list.get(reverseIndex(index));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new ReverseListIterator<>(list);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        list.add(reverseIndex(index) + 1, element);
    }

    @Override
    public T remove(int index) {
        return list.remove(reverseIndex(index));
    }

    @Override
    public T set(int index, T element) {
        return list.set(reverseIndex(index), element);
    }
}

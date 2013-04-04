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
package org.gitools.newick;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class ReverseListIterator<T> implements Iterable<T>, Iterator<T>
{

    private ListIterator<T> iterator;

    public ReverseListIterator(@NotNull List<T> list)
    {
        this.iterator = list.listIterator(list.size());
    }

    @NotNull
    @Override
    public Iterator<T> iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        return iterator.hasPrevious();
    }

    @Override
    public T next()
    {
        return iterator.previous();
    }

    @Override
    public void remove()
    {
        iterator.remove();
    }
}

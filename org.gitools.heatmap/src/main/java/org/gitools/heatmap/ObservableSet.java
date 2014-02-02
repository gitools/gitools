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
package org.gitools.heatmap;

import com.google.common.collect.ForwardingSet;

import java.util.Collection;
import java.util.Set;

public abstract class ObservableSet<T> extends ForwardingSet<T> {

    private Set<T> innerSet;

    public ObservableSet(Set<T> innerSet) {
        this.innerSet = innerSet;
    }

    @Override
    protected Set<T> delegate() {
        return innerSet;
    }

    @Override
    public boolean add(T element) {
        return fire(delegate().add(element));
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return fire(delegate().addAll(collection));
    }

    @Override
    public void clear() {
        delegate().clear();
        fire();
    }

    @Override
    public boolean remove(Object object) {
        return fire(delegate().remove(object));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return fire(delegate().removeAll(collection));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return fire(delegate().retainAll(collection));
    }

    private <T> T fire(T result) {
        fire();
        return result;
    }

    protected abstract void fire();

}

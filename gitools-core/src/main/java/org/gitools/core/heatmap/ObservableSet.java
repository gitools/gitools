package org.gitools.core.heatmap;

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
        return fire( delegate().add(element) );
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return fire( delegate().addAll(collection) );
    }

    @Override
    public void clear() {
        delegate().clear();
        fire();
    }

    @Override
    public boolean remove(Object object) {
        return fire( delegate().remove(object) );
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return fire( delegate().removeAll( collection) );
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return fire( delegate().retainAll( collection));
    }

    private <T> T fire(T result) {
        fire();
        return result;
    }

    protected abstract void fire();

}

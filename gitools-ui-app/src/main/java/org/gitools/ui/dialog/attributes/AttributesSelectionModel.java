/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.dialog.attributes;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @noinspection ALL
 */
class AttributesSelectionModel<T> implements ListModel {

    private final T[] attributes;
    private final List<Integer> selectedAttributes;
    private final List<Integer> unselectedAttributes;

    private final List<ListDataListener> listeners;

    public AttributesSelectionModel(T[] attributes) {
        this.attributes = attributes;
        this.selectedAttributes = new ArrayList<Integer>();
        this.unselectedAttributes = new ArrayList<Integer>();
        this.listeners = new ArrayList<ListDataListener>();

        for (int i = 0; i < attributes.length; i++)
            selectedAttributes.add(i);
    }

    @Override
    public int getSize() {
        return selectedAttributes.size();
    }

    @Override
    public Object getElementAt(int index) {
        return attributes[selectedAttributes.get(index)];
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public T[] getAttributes() {
        return attributes;
    }

    public List<Integer> getSelectedIndices() {
        return Collections.unmodifiableList(selectedAttributes);
    }

    public void setSelectedIndices(List<Integer> indices) {
        List<Integer> unselectAttributes = new ArrayList<Integer>(selectedAttributes);
        unselect(unselectAttributes);
        select(indices);
    }

    public List<Integer> getUnselectedIndices() {
        return Collections.unmodifiableList(unselectedAttributes);
    }


    public List<T> getSelectedAttributes() {
        List<T> attr = new ArrayList<T>(selectedAttributes.size());
        for (int i = 0; i < selectedAttributes.size(); i++)
            attr.add(attributes[selectedAttributes.get(i)]);

        return attr;
    }


    public List<T> getUnselectedAttributes() {
        List<T> attr = new ArrayList<T>(unselectedAttributes.size());
        for (int i = 0; i < unselectedAttributes.size(); i++)
            attr.add(attributes[unselectedAttributes.get(i)]);

        return attr;
    }

    public void select(List<Integer> indices) {
        for (Integer i : indices) {
            if (unselectedAttributes.contains(i)) {
                unselectedAttributes.remove(i);
                selectedAttributes.add(i);
            }
        }
        fireChange();
    }

    public void unselect(List<Integer> indices) {
        for (Integer i : indices) {
            if (selectedAttributes.contains(i)) {
                selectedAttributes.remove(i);
                unselectedAttributes.add(i);
            }
        }
        fireChange();
    }

    public void moveUp(List<Integer> indices) {

        for (Integer index : indices) {
            int i = selectedAttributes.indexOf(index);

            if (i >= 0) {
                Integer tmp = selectedAttributes.get(i);
                selectedAttributes.set(i, selectedAttributes.get(i - 1));
                selectedAttributes.set(i - 1, tmp);
            }
        }

        fireChange();
    }

    public void moveDown(List<Integer> indices) {

        for (Integer index : indices) {
            int i = selectedAttributes.indexOf(index);

            if (i >= 0) {
                Integer tmp = selectedAttributes.get(i);
                selectedAttributes.set(i, selectedAttributes.get(i + 1));
                selectedAttributes.set(i + 1, tmp);
            }
        }

        fireChange();
    }

    private void fireChange() {
        int size = selectedAttributes.size();

        ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, size > 0 ? size - 1 : 0);

        for (ListDataListener l : listeners)
            l.contentsChanged(e);
    }
}

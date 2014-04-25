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

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.view.Direction;
import org.gitools.api.matrix.view.IMatrixViewDimension;
import org.gitools.matrix.model.AbstractMatrixDimension;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.utils.xml.adapter.StringArrayXmlAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;

public abstract class AbstractMatrixViewDimension extends AbstractMatrixDimension implements IMatrixViewDimension {

    public static final String PROPERTY_FOCUS = "selectionLead";
    public static final String PROPERTY_SELECTED = "selected";
    public static final String PROPERTY_VISIBLE = "visible";

    @XmlElement(name = "visible")
    @XmlJavaTypeAdapter(StringArrayXmlAdapter.class)
    private List<String> visible;

    private transient Map<String, Integer> visibleToIndex;

    @XmlTransient
    private Set<String> selected;

    @XmlTransient
    private String focus;

    @XmlTransient
    private IMatrixDimension matrixDimension;

    public AbstractMatrixViewDimension() {
        this(null);
    }

    public AbstractMatrixViewDimension(IMatrixDimension matrixDimension) {
        super();

        setSelected(new HashSet<String>());
        this.focus = null;

        init(matrixDimension);
    }

    public void init(IMatrixDimension matrixDimension) {

        if (matrixDimension == null) {
            return;
        }

        setId(matrixDimension.getId());

        this.matrixDimension = matrixDimension;

        if (visible == null) {
            visible = new ArrayList<>(matrixDimension.size());
            showAll();
        } else {
            //TODO check that all the visible are valid identifiers
        }

        visibleToIndex = null;
    }

    @Override
    public void showAll() {
        visible.clear();

        for (String identifier : matrixDimension) {
            visible.add(identifier);
        }

        visibleToIndex = null;
    }

    public List<String> toList() {
        return Collections.unmodifiableList(visible);
    }

    public void show(List<String> identifiers) {

        // Update visible
        visible = new ArrayList<>(identifiers);
        visibleToIndex = null;

        // Update lead
        if (!visible.contains(focus)) {
            focus = null;
        }

        // Remove non visible identifiers from the selection
        setSelected(new HashSet<>(filter(identifiers, in(selected))));

        firePropertyChange(PROPERTY_VISIBLE, null, identifiers);
    }

    public void move(Direction direction, Set<String> identifiers) {

        if (identifiers == null || identifiers.isEmpty()) {
            return;
        }

        List<Integer> indices = new ArrayList<>(identifiers.size());
        for (String identifier : identifiers) {
            indices.add(visible.indexOf(identifier));
        }

        if (direction.getShift() == 1) {
            Collections.sort(indices, Collections.reverseOrder());

            // We cannot move the last position to the right
            if (indices.get(0) == size() - 1) {
                return;
            }

        } else {

            Collections.sort(indices);

            // We cannot move the first position to the left
            if (indices.get(0) == 0) {
                return;
            }
        }

        for (int idx : indices) {
            Collections.swap(visible, idx + direction.getShift(), idx);
        }

        visibleToIndex = null;

        firePropertyChange(PROPERTY_VISIBLE, null, visible);
        firePropertyChange(PROPERTY_SELECTED, null, selected);
    }

    public void hide(Set<String> identifiers) {
        visible.removeAll(identifiers);
        visibleToIndex = null;

        firePropertyChange(PROPERTY_VISIBLE, null, visible);
        firePropertyChange(PROPERTY_FOCUS, null, focus);
    }

    @Override
    public void hide(Predicate<String> predicate) {
        visible = Lists.newArrayList(filter(visible, not(predicate)));
        visibleToIndex = null;

        firePropertyChange(PROPERTY_VISIBLE, null, visible);
        firePropertyChange(PROPERTY_FOCUS, null, focus);
    }

    @Override
    public void sort(Comparator<String> comparator) {
        Collections.sort(visible, comparator);
        visibleToIndex = null;

        firePropertyChange(PROPERTY_VISIBLE, null, visible);
    }

    @Override
    public void show(Predicate<String> predicate) {
        visible = new ArrayList<>(filter(visible, predicate));
        visibleToIndex = null;

        firePropertyChange(PROPERTY_VISIBLE, null, visible);
    }

    @Override
    public Set<String> getSelected() {
        return selected;
    }

    @Override
    public void select(Set<String> selected) {
        setSelected(selected);
    }

    @Override
    public void select(Predicate<String> predicate) {
        setSelected(Sets.newHashSet(filter(visible, predicate)));
    }

    public void selectAll() {
        setSelected(new HashSet<>(visible));
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {

        if (Objects.equal(this.focus, focus)) {
            return;
        }

        this.focus = focus;

        firePropertyChange(PROPERTY_FOCUS, null, this.focus);
    }

    @Override
    public MatrixDimensionKey getId() {
        return matrixDimension.getId();
    }

    @Override
    public int size() {
        return visible.size();
    }

    @Override
    public String getLabel(int index) {
        if (index < 0 || index > size()) {
            return null;
        }

        return visible.get(index);
    }

    @Override
    public int indexOf(String label) {

        if (label == null) {
            return -1;
        }

        Integer index = getVisibleToIndex().get(label);
        return (index == null ? -1 : index);
    }

    @Override
    public Iterator<String> iterator() {
        return Iterators.unmodifiableIterator(visible.iterator());
    }

    private void setSelected(Set<String> selected) {
        this.selected = new ObservableSet<String>(selected) {
            @Override
            protected void fire() {
                firePropertyChange(PROPERTY_SELECTED, null, this);
            }
        };
        firePropertyChange(PROPERTY_SELECTED, null, this.selected);
    }

    private Map<String, Integer> getVisibleToIndex() {

        if (visibleToIndex == null) {
            visibleToIndex = new HashMap<>(visible.size());

            for (int i = 0; i < visible.size(); i++) {
                visibleToIndex.put(visible.get(i), i);
            }
        }

        return visibleToIndex;

    }

    @Override
    public IMatrixDimension subset(Set<String> identifiers) {
        return new HashMatrixDimension(getId(), Iterables.filter(visible, Predicates.in(identifiers)));
    }
}

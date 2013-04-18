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
package org.gitools.ui.sort;

import org.gitools.matrix.sort.ValueSortCriteria;
import org.gitools.matrix.sort.ValueSortCriteria.SortDirection;
import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ValueSortCriteriaTableModel implements TableModel {

    private static final String[] columnName = new String[]{"Attribute", "Agregation", "Direction"};

    private static final Class<?>[] columnClass = new Class<?>[]{String.class, CutoffCmp.class, SortDirection.class};

    @NotNull
    private final Map<String, Integer> attrIndexMap = new HashMap<String, Integer>();

    private final List<ValueSortCriteria> criteriaList;

    @NotNull
    private final List<TableModelListener> listeners = new ArrayList<TableModelListener>();

    private ValueSortCriteriaTableModel(List<ValueSortCriteria> criteriaList, @NotNull String[] attributeNames) {
        this.criteriaList = criteriaList;
        for (int i = 0; i < attributeNames.length; i++)
            attrIndexMap.put(attributeNames[i], i);
    }

    public ValueSortCriteriaTableModel(@NotNull String[] attributeNames) {
        this(new ArrayList<ValueSortCriteria>(), attributeNames);
    }

    @Override
    public int getRowCount() {
        return criteriaList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnName[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Nullable
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return criteriaList.get(rowIndex).getAttributeName();
            case 1:
                return criteriaList.get(rowIndex).getAggregator();
            case 2:
                return criteriaList.get(rowIndex).getDirection();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                String attrName = (String) aValue;
                criteriaList.get(rowIndex).setAttributeName(attrName);
                Integer index = attrIndexMap.get(attrName);
                criteriaList.get(rowIndex).setAttributeIndex(index != null ? index : 0);
                break;

            case 1:
                criteriaList.get(rowIndex).setAggregator((IAggregator) aValue);
                break;

            case 2:
                criteriaList.get(rowIndex).setDirection((SortDirection) aValue);
                break;
        }
    }

    public List<ValueSortCriteria> getList() {
        return criteriaList;
    }

    public void addCriteria(final ValueSortCriteria criteria) {
        criteriaList.add(criteria);
        fireCriteriaChanged();
    }

    void addAllCriteria(@NotNull List<ValueSortCriteria> list) {
        //int initialRow = criteriaList.size();
        criteriaList.addAll(list);
        fireCriteriaChanged();
    }

    void removeCriteria(@NotNull int[] selectedRows) {
        List<Object> objects = new ArrayList<Object>(selectedRows.length);
        for (int index : selectedRows)
            objects.add(criteriaList.get(index));

        criteriaList.removeAll(objects);
        fireCriteriaChanged();
    }

    private void fireCriteriaChanged() {
        TableModelEvent e = new TableModelEvent(this);
        for (TableModelListener l : listeners)
            l.tableChanged(e);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
}

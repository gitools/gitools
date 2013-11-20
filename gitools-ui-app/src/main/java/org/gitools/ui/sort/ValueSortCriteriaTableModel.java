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

import org.gitools.api.analysis.IAggregator;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.SortDirection;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ValueSortCriteriaTableModel implements TableModel {

    private static final String[] columnName = new String[]{"Attribute", "Agregation", "Direction"};

    private static final Class<?>[] columnClass = new Class<?>[]{String.class, CutoffCmp.class, SortDirection.class};

    private final IMatrixLayers<IMatrixLayer> layers;

    private final List<IMatrixLayer> criteriaList;

    private final List<TableModelListener> listeners = new ArrayList<TableModelListener>();

    public ValueSortCriteriaTableModel(IMatrixLayers<IMatrixLayer> layers, IMatrixLayer... initialCriteria) {
        super();

        this.criteriaList = new ArrayList<>(initialCriteria.length);
        Collections.addAll(this.criteriaList, initialCriteria);
        this.layers = layers;
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return criteriaList.get(rowIndex).getId();
            case 1:
                return criteriaList.get(rowIndex).getAggregator();
            case 2:
                return criteriaList.get(rowIndex).getSortDirection();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                String attrName = (String) aValue;
                criteriaList.set(rowIndex, layers.get(attrName));
                break;

            case 1:
                criteriaList.get(rowIndex).setAggregator((IAggregator) aValue);
                break;

            case 2:
                criteriaList.get(rowIndex).setSortDirection((SortDirection) aValue);
                break;
        }
    }

    public List<IMatrixLayer> getList() {
        return criteriaList;
    }

    public void addCriteria(final IMatrixLayer criteria) {
        criteriaList.add(criteria);
        fireCriteriaChanged();
    }

    void removeCriteria(int[] selectedRows) {
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

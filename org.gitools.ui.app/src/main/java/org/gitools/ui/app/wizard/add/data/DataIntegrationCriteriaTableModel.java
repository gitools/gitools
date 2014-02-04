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
package org.gitools.ui.app.wizard.add.data;

import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.matrix.filter.DataIntegrationCriteria;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DataIntegrationCriteriaTableModel implements TableModel {

    private static final String[] columnName = new String[]{"Operator", "Layer", "Condition", "Value"};

    private static final Class<?>[] columnClass = new Class<?>[]{String.class, CutoffCmp.class, String.class, Operator.class};


    private final Map<String, Integer> layerIndexMap = new HashMap<>();

    private final List<DataIntegrationCriteria> criteriaList;


    private final List<TableModelListener> listeners = new ArrayList<>();
    private final IMatrixLayers layers;

    private DataIntegrationCriteriaTableModel(List<DataIntegrationCriteria> criteriaList, IMatrixLayers layers) {
        this.criteriaList = criteriaList;
        this.layers = layers;
        for (int i = 0; i < layers.size(); i++)
            layerIndexMap.put(layers.getIds()[i], i);
    }

    public DataIntegrationCriteriaTableModel(IMatrixLayers layers) {
        this(new ArrayList<DataIntegrationCriteria>(), layers);
    }

    @Override
    public int getRowCount() {
        return criteriaList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
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
                return criteriaList.get(rowIndex).getOperator();
            case 1:
                return criteriaList.get(rowIndex).getAttributeName();
            case 2:
                return criteriaList.get(rowIndex).getComparator();
            case 3:
                return String.valueOf(criteriaList.get(rowIndex).getValue());

        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                if (rowIndex > 0) {
                    criteriaList.get(rowIndex).setOperator(Operator.getFromName((String) aValue));
                } else {
                    criteriaList.get(rowIndex).setOperator(Operator.EMPTY);
                }
                break;
            case 1:
                String layerId = (String) aValue;
                criteriaList.get(rowIndex).setLayer(layers.get(layerId));
                //Integer index = layerIndexMap.get(layerId);
                //criteriaList.get(rowIndex).setAttributeIndex(index != null ? index : 0);
                break;

            case 2:
                criteriaList.get(rowIndex).setComparator((CutoffCmp) aValue);
                break;

            case 3:
                criteriaList.get(rowIndex).setValue(Double.parseDouble((String) aValue));
                break;
        }
    }

    public List<DataIntegrationCriteria> getList() {
        return criteriaList;
    }

    public void addCriteria(final DataIntegrationCriteria criteria) {
        criteriaList.add(criteria);
        fireCriteriaChanged();
    }

    void addAllCriteria(List<DataIntegrationCriteria> list) {
        int initialRow = criteriaList.size();
        criteriaList.addAll(list);
        fireCriteriaChanged();
    }

    void removeCriteria(int[] selectedRows) {
        List<Object> objects = new ArrayList<>(selectedRows.length);
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

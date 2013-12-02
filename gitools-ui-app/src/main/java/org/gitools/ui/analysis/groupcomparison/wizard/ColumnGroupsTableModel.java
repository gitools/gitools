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
package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.ColumnGroup;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ColumnGroupsTableModel extends AbstractTableModel{

    private static final String[] columnName = new String[]{"Name", "Property", "GroupNumber"};

    private static final Class<?>[] columnClass = new Class<?>[]{String.class, String.class, Integer.class};

    private List<IndexedGroup> indexedGroupList = null;

    private class IndexedGroup implements Comparable<IndexedGroup> {
        ColumnGroup group;
        int index;

        public IndexedGroup(ColumnGroup group, int index) {
            this.group = group;
            this.index = index;
        }

        public ColumnGroup getGroup() {
            return group;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public int compareTo(IndexedGroup o) {
            return this.index - o.getIndex();
        }
    }

    public ColumnGroupsTableModel() {
        super();
    }

    public void setGroups(ColumnGroup[] groups)  {
        indexedGroupList = new ArrayList<>(groups.length);
        for (int i = 0; i < groups.length; i++) {
            indexedGroupList.add(new IndexedGroup(groups[i], i+1));
        }

        fireTableDataChanged();
    }


    @Override
    public int getRowCount() {
        return indexedGroupList.size();
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
        if (columnIndex != 1 & rowIndex > -1) {
            return true;
        }
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return indexedGroupList.get(rowIndex).getGroup().getName();
            case 1:
                return indexedGroupList.get(rowIndex).getGroup().getProperty();
            case 2:
                return indexedGroupList.get(rowIndex).getIndex();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 2:
                indexedGroupList.get(rowIndex).setIndex((int) aValue);
                break;
            case 0:
            case 1:
                indexedGroupList.set(rowIndex, (IndexedGroup) aValue);
                break;
        }
    }

    public List<ColumnGroup> getList() {
        List list = new ArrayList<ColumnGroup>();

        for (IndexedGroup g : indexedGroupList)
            list.add(g.getGroup());

        return list;
    }

    public void addGroup(final ColumnGroup group) {
        indexedGroupList.add(new IndexedGroup(group, indexedGroupList.size()));
        fireTableDataChanged();
    }

    @Override
    public void fireTableDataChanged() {
        reorder();
        super.fireTableDataChanged();
    }

    private void reorder() {
        Collections.sort(indexedGroupList);
        int next = 1;
        int currentGroup;
        int lastGroup = -1;
        for (IndexedGroup g : indexedGroupList) {
            currentGroup = g.getIndex();
            if (currentGroup != lastGroup) {
                g.setIndex(next++);
            }
            lastGroup = currentGroup;
        }
    }

    void removeGroup(int[] selectedRows) {
        List<Object> objects = new ArrayList<>(selectedRows.length);
        for (int index : selectedRows) {
            objects.add(indexedGroupList.get(index));
            indexedGroupList.remove(index);
        }


        indexedGroupList.removeAll(objects);
        fireTableDataChanged();
    }

}

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
package org.gitools.ui.app.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.dimensiongroups.DimensionGroup;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DimensionGroupTableModel extends AbstractTableModel {

    private static final String[] columnName = new String[]{"Name", "Property", "Group #"};

    private static final Class<?>[] columnClass = new Class<?>[]{String.class, String.class, Integer.class};

    private List<IndexedGroup> indexedGroupList = null;

    private class IndexedGroup implements Comparable<IndexedGroup> {
        DimensionGroup group;
        int index;

        public IndexedGroup(DimensionGroup group, int index) {
            this.group = group;
            this.index = index;
        }

        public DimensionGroup getGroup() {
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

    public DimensionGroupTableModel() {
        super();
        indexedGroupList = new ArrayList<IndexedGroup>(0);
    }

    public void setGroups(DimensionGroup[] groups) {
        indexedGroupList = new ArrayList<>(groups.length);
        for (int i = 0; i < groups.length; i++) {
            indexedGroupList.add(new IndexedGroup(groups[i], i + 1));
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
            case 0:
                indexedGroupList.get(rowIndex).getGroup().setName((String) aValue);
                break;
            case 1:
                indexedGroupList.set(rowIndex, (IndexedGroup) aValue);
                break;
            case 2:
                indexedGroupList.get(rowIndex).setIndex((int) aValue);
                break;
        }
    }

    public List<DimensionGroup> getGroupList() {
        List list = new ArrayList<DimensionGroup>();

        for (IndexedGroup g : indexedGroupList)
            list.add(g.getGroup());

        return list;
    }

    public void addGroup(final DimensionGroup group) {
        indexedGroupList.add(new IndexedGroup(group, indexedGroupList.size() + 1));
        fireTableDataChanged();
    }

    public void setGroup(final DimensionGroup group, int position) {
        int groupIndex = indexedGroupList.get(position).getIndex();
        indexedGroupList.set(position, new IndexedGroup(group, groupIndex));
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
        int lastSet = -1;
        int currentGroup;
        int lastGroup = -1;
        for (IndexedGroup g : indexedGroupList) {
            currentGroup = g.getIndex();
            if (currentGroup != lastGroup) {
                g.setIndex(next++);
                lastSet = g.getIndex();
            } else {
                g.setIndex(lastSet);
            }
            lastGroup = currentGroup;
        }
    }

    void removeGroups(int[] selectedRows) {
        List<Object> objects = new ArrayList<>(selectedRows.length);
        for (int index : selectedRows) {
            objects.add(indexedGroupList.get(index));
        }

        indexedGroupList.removeAll(objects);
        fireTableDataChanged();
    }

    public DimensionGroup getGroupAt(int index) {
        return indexedGroupList.get(index).getGroup();
    }

}

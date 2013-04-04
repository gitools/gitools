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
package org.gitools.ui.heatmap.header.wizard.coloredlabels;

import org.gitools.heatmap.header.ColoredLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class ColoredLabelsTableModel implements TableModel
{

    private static final String[] columnName = new String[]{
            "Value", "Displayed Label", "Color"};

    private static final Class<?>[] columnClass = new Class<?>[]{
            String.class, String.class, Color.class};

    private List<ColoredLabel> labelList;

    @NotNull
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    private boolean valueEditable = true;
    private boolean valueMustBeNumeric = false;

    public ColoredLabelsTableModel(@NotNull ColoredLabel[] coloredLabels)
    {
        List<ColoredLabel> list = new ArrayList<ColoredLabel>();
        for (ColoredLabel cl : coloredLabels)
            list.add(cl);
        this.labelList = list;
    }

    public ColoredLabelsTableModel()
    {
        this.labelList = new ArrayList<ColoredLabel>();
    }

    public ColoredLabelsTableModel(List<ColoredLabel> coloredLabels)
    {
        this.labelList = coloredLabels;
    }

    public void setValueEditable(boolean editable)
    {
        this.valueEditable = editable;
    }

    public boolean isValueEditable()
    {
        return this.valueEditable;
    }

    public boolean isValueMustBeNumeric()
    {
        return valueMustBeNumeric;
    }

    public void setValueMustBeNumeric(boolean valueMustBeNumeric)
    {
        this.valueMustBeNumeric = valueMustBeNumeric;
    }

    @Override
    public int getRowCount()
    {
        return labelList.size();
    }

    @Override
    public int getColumnCount()
    {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnName[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
        {
            return this.isValueEditable();
        }
        return true;
    }

    @Nullable
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return labelList.get(rowIndex).getValue();
            case 1:
                return labelList.get(rowIndex).getDisplayedLabel();
            case 2:
                return labelList.get(rowIndex).getColor();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                String value = (String) aValue;
                try
                {
                    if (this.valueMustBeNumeric)
                    {
                        Double.parseDouble(value);
                    }
                    labelList.get(rowIndex).setValue(value);
                } catch (NumberFormatException e)
                {
                    // do nothing
                }
                break;

            case 1:
                String displayedLabel = (String) aValue;
                labelList.get(rowIndex).setDisplayedLabel(displayedLabel);
                break;

            case 2:
                labelList.get(rowIndex).setColor((Color) aValue);
                break;
        }
    }

    public List<ColoredLabel> getList()
    {
        return labelList;
    }

    public void addLabel(final ColoredLabel coloredLabel)
    {
        labelList.add(coloredLabel);
        fireContentChanged();
    }

    void addAllLabels(@NotNull List<ColoredLabel> list)
    {
        int initialRow = labelList.size();
        labelList.addAll(list);
        fireContentChanged();
    }

    void addAllLabels(@NotNull ColoredLabel[] coloredLabels)
    {
        List<ColoredLabel> list = new ArrayList<ColoredLabel>();
        for (ColoredLabel cl : coloredLabels)
            list.add(cl);
        this.labelList = list;
        fireContentChanged();
    }

    void removeLabel(@NotNull int[] rowsToRemove)
    {
        List<Object> objects = new ArrayList<Object>(rowsToRemove.length);
        for (int index : rowsToRemove)
            objects.add(labelList.get(index));

        labelList.removeAll(objects);
        fireContentChanged();
    }

    private void fireContentChanged()
    {
        TableModelEvent e = new TableModelEvent(this);
        for (TableModelListener l : listeners)
            l.tableChanged(e);
    }

    @Override
    public void addTableModelListener(TableModelListener l)
    {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l)
    {
        listeners.remove(l);
    }
}

/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.heatmap.header.coloredlabels;

import org.gitools.heatmap.header.ColoredLabel;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

class ColoredLabelsTableModel implements TableModel {

	private static final String[] columnName = new String[] {
		"Value","Description", "Color" };

	private static final Class<?>[] columnClass = new Class<?>[] {
		String.class, String.class, Color.class};

	private List<ColoredLabel> labelList;

	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    private boolean valueEditable = true;
    private boolean valueMustBeNumeric = false;

    public ColoredLabelsTableModel(ColoredLabel[] coloredLabels) {
        List<ColoredLabel> list = new ArrayList<ColoredLabel>();
        for (ColoredLabel cl : coloredLabels)
            list.add(cl);
        this.labelList = list;
    }

    public ColoredLabelsTableModel() {
        this.labelList = new ArrayList<ColoredLabel>();
    }

    public ColoredLabelsTableModel(List<ColoredLabel> coloredLabels) {
		this.labelList = coloredLabels;
	}

    public void setValueEditable(boolean editable) {
        this.valueEditable = editable;
    }

    public boolean isValueEditable() {
        return this.valueEditable;
    }

    public boolean isValueMustBeNumeric() {       
        return valueMustBeNumeric;
    }

    public void setValueMustBeNumeric(boolean valueMustBeNumeric) {
        this.valueMustBeNumeric = valueMustBeNumeric;
    }

	@Override
	public int getRowCount() {
		return labelList.size();
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
        if (columnIndex==0) {
            return this.isValueEditable();
        }
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
            case 0: return labelList.get(rowIndex).getValue();
			case 1: return labelList.get(rowIndex).getDescription();
			case 2: return labelList.get(rowIndex).getColor();
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
            case 0:
                String value = (String) aValue;
                try {
                    if (this.valueMustBeNumeric)
                        Double.parseDouble(value);
                    labelList.get(rowIndex).setValue(value);
                } catch (NumberFormatException e) {
                    // do nothing
                }
            break;

			case 1:
				String description = (String) aValue;
                labelList.get(rowIndex).setDescription(description);
			break;

			case 2:
				labelList.get(rowIndex).setColor((Color) aValue);
			break;
        }
	}

	public List<ColoredLabel> getList() {
		return labelList;
	}

	public void addLabel(final ColoredLabel coloredLabel) {
		labelList.add(coloredLabel);
		fireContentChanged();
	}

	void addAllLabels(List<ColoredLabel> list) {
		int initialRow = labelList.size();
		labelList.addAll(list);
		fireContentChanged();
	}

    void addAllLabels(ColoredLabel[] coloredLabels) {
        List<ColoredLabel> list = new ArrayList<ColoredLabel>();
        for (ColoredLabel cl : coloredLabels)
                list.add(cl);
        this.labelList = list;
        fireContentChanged();
    }

	void removeLabel(int[] rowsToRemove) {
		List<Object> objects = new ArrayList<Object>(rowsToRemove.length);
		for (int index : rowsToRemove)
			objects.add(labelList.get(index));

		labelList.removeAll(objects);
		fireContentChanged();
	}

	private void fireContentChanged() {
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

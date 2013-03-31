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

package org.gitools.ui.wizard.add.data;

import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.gitools.matrix.data.integration.DataIntegrationCriteria;

class DataIntegrationCriteriaTableModel implements TableModel {

	private static final String[] columnName = new String[] {
		"Operator","Attribute", "Condition", "Value" };

	private static final Class<?>[] columnClass = new Class<?>[] {
		String.class, CutoffCmp.class, String.class, Operator.class};

	private Map<String, Integer> attrIndexMap = new HashMap<String, Integer>();

	private List<DataIntegrationCriteria> criteriaList;

	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	public DataIntegrationCriteriaTableModel(List<DataIntegrationCriteria> criteriaList, String[] attributeNames) {
		this.criteriaList = criteriaList;
		for (int i = 0; i < attributeNames.length; i++)
			attrIndexMap.put(attributeNames[i], i);
	}

	public DataIntegrationCriteriaTableModel(String[] attributeNames) {
		this(new ArrayList<DataIntegrationCriteria>(), attributeNames);
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
                        case 0: return criteriaList.get(rowIndex).getOperator();
			case 1: return criteriaList.get(rowIndex).getAttributeName();
			case 2: return criteriaList.get(rowIndex).getComparator();
			case 3: return String.valueOf(criteriaList.get(rowIndex).getCutoffValue());

		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
                        case 0:
                                if (rowIndex > 0)
                                    criteriaList.get(rowIndex).setOperator(Operator.getFromName((String) aValue));
                                else
                                    criteriaList.get(rowIndex).setOperator(Operator.EMPTY);
                        break;
			case 1:
				String attrName = (String) aValue;
				criteriaList.get(rowIndex).setAttributeName(attrName);
				Integer index = attrIndexMap.get(attrName);
				criteriaList.get(rowIndex).setAttributeIndex(index != null ? index : 0);
			break;

			case 2:
				criteriaList.get(rowIndex).setComparator((CutoffCmp) aValue);
			break;

			case 3:
				criteriaList.get(rowIndex).setCutoffValue(Double.parseDouble((String) aValue));
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

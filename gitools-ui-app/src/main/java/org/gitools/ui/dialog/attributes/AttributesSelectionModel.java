/*
 *  Copyright 2010 cperez.
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

package org.gitools.ui.dialog.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class AttributesSelectionModel<T> implements ListModel {

	private T[] attributes;
	private List<Integer> selectedAttributes;
	private List<Integer> unselectedAttributes;

	private List<ListDataListener> listeners;

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

		ListDataEvent e = new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED,
				0, size > 0 ? size - 1 : 0);
		
		for (ListDataListener l : listeners)
			l.contentsChanged(e);
	}
}

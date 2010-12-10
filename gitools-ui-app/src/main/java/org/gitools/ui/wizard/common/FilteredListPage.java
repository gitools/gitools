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

package org.gitools.ui.wizard.common;


import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class FilteredListPage extends AbstractWizardPage {

	private FilteredListPanel panel;

	public FilteredListPage() {
	}
	
	public FilteredListPage(Object[] listData) {
		panel.setListData(listData);
	}

	public FilteredListPage(Object[] listData, int selectionMode) {
		panel.setListData(listData);
		panel.setSelectionMode(selectionMode);
	}

	@Override
	public JComponent createControls() {
		panel = new FilteredListPanel() {
			@Override protected void selectionChanged() {
				FilteredListPage.this.selectionChanged(); } };
		
		return panel;
	}

	private void selectionChanged() {
		Object value = panel.getSelectedValue();
		setComplete(value != null);
	}

	public void setSelectionMode(int mode) {
		panel.setSelectionMode(mode);
	}

	public String getFilterText() {
		return panel.getFilterText();
	}
	
	protected void setListData(Object[] listData) {
		panel.setListData(listData);
	}
	
	public Object getSelectedValue() {
		return panel.getSelectedValue();
	}

	public void setSelectedValue(Object o) {
		panel.setSElectedValue(o);
	}
}

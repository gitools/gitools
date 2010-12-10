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

package org.gitools.ui.intogen.wizard.modules;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenOncomodulesListPage extends AbstractWizardPage {
	
	private IntogenOncomodulesListPanel panel;
	private DefaultListModel listModel;

	public IntogenOncomodulesListPage() {
		listModel = new DefaultListModel();
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenOncomodulesListPanel();

		panel.list.setModel(listModel);
		updateComplete();
		
		panel.list.getModel().addListDataListener(new ListDataListener() {
			@Override public void contentsChanged(ListDataEvent e) {
				updateComplete(); }
			@Override public void intervalAdded(ListDataEvent e) {
				updateComplete(); }
			@Override public void intervalRemoved(ListDataEvent e) {
				updateComplete(); }
		});
		
		return panel;
	}
	
	private void updateComplete() {
		setComplete(panel.list.getModel().getSize() != 0);
	}
	
	public DefaultListModel getListModel() {
		return listModel;
	}

}

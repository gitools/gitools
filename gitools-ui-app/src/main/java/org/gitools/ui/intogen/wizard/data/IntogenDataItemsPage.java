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

package org.gitools.ui.intogen.wizard.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenDataItemsPage extends AbstractWizardPage {

	private IntogenDataItemsPanel panel;

	public IntogenDataItemsPage() {
		setTitle("Select items type");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenDataItemsPanel();
		
		panel.itemsCbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.annList.removeAll();
			}
		});
		
		panel.annChk.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				boolean isSel = panel.annChk.isSelected();
				panel.annList.setEnabled(isSel);
				panel.annAddBtn.setEnabled(isSel);
				panel.annRemoveBtn.setEnabled(isSel);
			}
		});
		
		return panel;
	}

}

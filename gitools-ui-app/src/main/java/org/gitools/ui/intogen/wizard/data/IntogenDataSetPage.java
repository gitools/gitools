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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenDataSetPage extends AbstractWizardPage {

	private IntogenDataSetPanel panel;

	public IntogenDataSetPage() {
		setTitle("Select data set");
	}
	
	@Override
	public JComponent createControls() {
		ItemListener itemListener = new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				updateComplete();
			}
		};
		
		panel = new IntogenDataSetPanel();
		panel.samplesRbt.addItemListener(itemListener);
		panel.experimentsRbt.addItemListener(itemListener);
		panel.combinationsRbt.addItemListener(itemListener);
		
		return panel;
	}

	private void updateComplete() {
		setComplete(panel.samplesRbt.isSelected() 
				|| panel.experimentsRbt.isSelected()
				|| panel.combinationsRbt.isSelected());
	}

	public boolean isSamplesSelected() {
		return panel.samplesRbt.isSelected();
	}
	
	public boolean isExperimentsSelected() {
		return panel.experimentsRbt.isSelected();
	}
	
	public boolean isCombinationsSelected() {
		return panel.combinationsRbt.isSelected();
	}
}

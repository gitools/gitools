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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenOncomoduleSetPage extends AbstractWizardPage {

	private IntogenOncomoduleSetPanel panel;

	public IntogenOncomoduleSetPage() {
		super();
		
		setTitle("Select oncomodules set");
	}
	
	@Override
	public JComponent createControls() {
		ActionListener listener = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				updateComplete();
			}
		};
		
		panel = new IntogenOncomoduleSetPanel();
		panel.combinationsRbt.addActionListener(listener);
		panel.experimentsRbt.addActionListener(listener);
		return panel;
	}

	private void updateComplete() {
		boolean isCompleted = panel.experimentsRbt.isSelected()
						|| panel.combinationsRbt.isSelected();
		setComplete(isCompleted);
	}

	public boolean isExperimentsSelected() {
		return panel.experimentsRbt.isSelected();
	}
	
	public boolean isCombinationsSelected() {
		return panel.combinationsRbt.isSelected();
	}
}

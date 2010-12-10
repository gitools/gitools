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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

@Deprecated
public class IntogenOncomodulePage extends AbstractWizardPage {

	private IntogenOncomodulePanel panel;

	public IntogenOncomodulePage() {
		setTitle("Download modules from IntOGen");
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenOncomodulePanel();
		
		panel.sourceCbox.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				boolean isExp = panel.sourceCbox.getSelectedItem()
								.toString().equals("Experiment");
				panel.experimentLabel.setVisible(isExp);
				panel.experimentCbox.setVisible(isExp);
			}
			
		});
		
		return panel;
	}

}

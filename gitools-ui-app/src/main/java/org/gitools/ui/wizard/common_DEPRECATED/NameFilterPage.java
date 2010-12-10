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

package org.gitools.ui.wizard.common_DEPRECATED;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class NameFilterPage extends AbstractWizardPage {

	private NameFilterPanel panel;

	public NameFilterPage() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JComponent createControls() {
		panel = new NameFilterPanel();
		return panel;
	}

}

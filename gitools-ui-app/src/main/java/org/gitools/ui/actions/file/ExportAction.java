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

package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.export.ExportWizard;

public class ExportAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportAction() {
		super("Export...");
		
		setDesc("Export...");
		setMnemonic(KeyEvent.VK_X);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new ExportWizard());
		
		wizDlg.open();
		
		AppFrame.instance().setStatusText("Done.");
	}
}

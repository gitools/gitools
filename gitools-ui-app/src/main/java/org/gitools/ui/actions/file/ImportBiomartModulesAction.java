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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;
import org.gitools.biomart.BiomartService;
import org.gitools.biomart.restful.model.Query;

import org.gitools.ui.IconNames;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.biomart.wizard.BiomartModulesWizard;
import org.gitools.ui.platform.progress.JobRunnable;

public class ImportBiomartModulesAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportBiomartModulesAction() {
		super("Biomart Modules (advanced users) ...");
		setLargeIconFromResource(IconNames.biomart24);
		setSmallIconFromResource(IconNames.biomart16);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

			final BiomartModulesWizard wizard = new BiomartModulesWizard();
			WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wizard);
			wdlg.open();
			if (wdlg.isCancelled())
				return;

			final File file = wizard.getSelectedFile();
			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					monitor.begin("Downloading data...", 1);
					Query query = wizard.getQuery();
					String format = (String) wizard.getFormat().getMime();
					BiomartService service = wizard.getService();
					try {
						service.queryModule(query, file, format, monitor);
					} catch (Exception ex) {
						monitor.exception(ex);
					}
					monitor.end();
				}
			});		 
	}
}

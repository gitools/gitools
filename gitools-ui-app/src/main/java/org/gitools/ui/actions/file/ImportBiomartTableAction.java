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
import org.gitools.ui.biomart.wizard.BiomartTableWizard;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ImportBiomartTableAction extends BaseAction {

	private static final long serialVersionUID = 4381993756203388654L;

	public ImportBiomartTableAction() {
		super("Biomart Table ...");
		setLargeIconFromResource(IconNames.biomart24);
		setSmallIconFromResource(IconNames.biomart16);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

			//final IBiomartService service = BiomartServiceFactory.createDefaultservice();
			//final BiomartCentralPortalSoapService service = BiomartCentralPortalSoapService.getDefault();
			//List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();
			//BiomartSource bsrc = lBs.get(0);
			//final BiomartRestfulService service = BiomartServiceFactory.createRestfulService(bsrc);
		
			final BiomartTableWizard wizard = new BiomartTableWizard();

			WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wizard);
			wdlg.open();
			if (wdlg.isCancelled()) {
				return;
			}
			final File file = wizard.getSelectedFile();
			JobThread.execute(AppFrame.instance(), new JobRunnable() {
 
				@Override
				public void run(IProgressMonitor monitor) {
					monitor.begin("Downloading data...", 1);
					Query query = wizard.getQuery();
					String format = (String) wizard.getFormat().getMime();
					BiomartService service = wizard.getService();
					try {
						service.queryTable(query, file, format, wizard.isSkipRowsWithEmptyValuesEnabled(), wizard.emptyValuesReplacement(), monitor);
					} catch (Exception ex) {
						monitor.exception(ex);
					}
					monitor.end();
				}
			});

	}

}

/*
 *  Copyright 2010 chris.
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
import org.gitools.kegg.modules.EnsemblKeggModulesImporter;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.IconNames;
import org.gitools.ui.modules.wizard.ModulesImportWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ImportKeggModulesAction extends BaseAction {

	public ImportKeggModulesAction() {
		super("KEGG Pathways ...");
		setLargeIconFromResource(IconNames.KEGG24);
		setSmallIconFromResource(IconNames.KEGG16);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final EnsemblKeggModulesImporter importer =
				new EnsemblKeggModulesImporter(true, false);

		final ModulesImportWizard wz = new ModulesImportWizard(importer);
		wz.setTitle("Import KEGG modules...");
		wz.setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_KEGG, 96));

		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wz);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				try {
					ModuleMap mmap = importer.importMap(monitor);
					if (!monitor.isCancelled()) {
						String mime = wz.getSaveFilePage().getFormat().getMime();
						File file = wz.getSaveFilePage().getFile();
						if (MimeTypes.GENE_MATRIX.equals(mime)
								|| MimeTypes.GENE_MATRIX_TRANSPOSED.equals(mime)) {

							BaseMatrix mat = MatrixUtils.moduleMapToMatrix(mmap);
							PersistenceManager.getDefault().store(file, mime, mat, monitor);
						}
						else
							PersistenceManager.getDefault().store(file, mime, mmap, monitor);
					}
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});
	}
}

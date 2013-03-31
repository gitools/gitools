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

package org.gitools.ui.modules.wizard;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import javax.swing.SwingUtilities;
import org.gitools.modules.importer.ModuleCategory;
import org.gitools.modules.importer.ModulesImporter;
import org.gitools.modules.importer.Organism;
import org.gitools.modules.importer.Version;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.wizard.common.FilteredListPage;

public class ModulesOrganismPage extends FilteredListPage {

	private ModulesImporter importer;

	private ModuleCategory modCategory;
	private Version version;
	boolean loaded;

	public ModulesOrganismPage(ModulesImporter importer) {
		this.importer = importer;
		this.loaded = false;

		setTitle("Select organism");
	}

	@Override
	public void updateControls() {
		super.updateControls();

		if (!loaded || modCategory != importer.getModuleCategory()
				|| version != importer.getVersion()) {
			
			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						monitor.begin("Getting available organisms ...", 1);

						modCategory = importer.getModuleCategory();
						version = importer.getVersion();
						final Organism[] organisms = importer.getOrganisms();
						SwingUtilities.invokeLater(new Runnable() {
							@Override public void run() {
								setListData(organisms);
								for (Organism o : organisms)
									if (o.getName().equals("homo sapiens"))
										setSelectedValue(o);
								loaded = true;
							}
						});

						monitor.end();
					}
					catch (Exception ex) {
						monitor.exception(ex);
					}
				}
			});
		}
	}

	@Override
	public void updateModel() {
		importer.setOrganism(getOrganism());
	}

	private Organism getOrganism() {
		return (Organism) getSelectedValue();
	}
}

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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import javax.swing.SwingUtilities;
import org.gitools.biomart.idmapper.EnsemblIds;
import org.gitools.modules.importer.FeatureCategory;
import org.gitools.modules.importer.ModulesImporter;
import org.gitools.modules.importer.Organism;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.wizard.common.FilteredListPage;

public class ModulesFeaturesPage extends FilteredListPage {

	private final ModulesImporter importer;

	private Organism organism;
	private boolean loaded;

	public ModulesFeaturesPage(ModulesImporter importer) {
		this.importer = importer;

		setTitle("Select Identifiers");
	}

	@Override
	public void updateControls() {
		if (!loaded || organism != importer.getOrganism()) {
			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						monitor.begin("Getting available identifiers ...", 1);

						organism = importer.getOrganism();
						final FeatureCategory[] features = importer.getFeatureCategories();
						SwingUtilities.invokeLater(new Runnable() {
							@Override public void run() {
								setListData(features);
								for (FeatureCategory f : features)
									if (f.getRef().equals(EnsemblIds.ENSEMBL_GENES))
										setSelectedValue(f);
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
		importer.setFeatCategory(getFeatureCategory());
	}

	private FeatureCategory getFeatureCategory() {
		return (FeatureCategory) getSelectedValue();
	}
}

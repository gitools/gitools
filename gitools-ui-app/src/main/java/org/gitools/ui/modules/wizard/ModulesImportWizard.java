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

package org.gitools.ui.modules.wizard;

import org.gitools.modules.importer.ModulesImporter;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

public class ModulesImportWizard extends AbstractWizard {

	private FileFormat[] supportedFormats = new FileFormat[] {
		FileFormats.MODULES_2C_MAP,
		FileFormats.GENE_MATRIX,
		FileFormats.GENE_MATRIX_TRANSPOSED,
	};

	private ModulesImporter importer;

	private ModulesSourcePage moduleCategoryPage;
	private ModulesOrganismPage organismPage;
	private ModulesFeaturesPage featuresPage;
	private SaveFilePage saveFilePage;

	public ModulesImportWizard(ModulesImporter importer) {
		this.importer = importer;

		setTitle("Import modules...");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_MODULES, 96));
	}

	@Override
	public void addPages() {

		moduleCategoryPage = new ModulesSourcePage(importer);
		addPage(moduleCategoryPage);

		organismPage = new ModulesOrganismPage(importer);
		addPage(organismPage);

		featuresPage = new ModulesFeaturesPage(importer);
		addPage(featuresPage);

		// Destination
		saveFilePage = new SaveFilePage() {
			@Override public void updateModel() {
				Settings.getDefault().setLastMapPath(getFolder()); } };
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastMapPath());
		saveFilePage.setFormats(supportedFormats);
		addPage(saveFilePage);
	}

	@Override
	public void pageEntered(IWizardPage page) {
		if (saveFilePage.equals(page))
			if (saveFilePage.getFileName().isEmpty())
				saveFilePage.setFileName(automaticFileName(importer));
	}

	private String automaticFileName(ModulesImporter importer) {
		StringBuilder sb = new StringBuilder();
		sb.append(importer.getOrganism().getName().replace(' ', '_'));
		sb.append("__");
		sb.append(importer.getModuleCategory().getRef().replace(':', '_'));
		sb.append("__");
		sb.append(importer.getFeatureCategory().getRef().replace(':', '_'));
		return sb.toString();
	}

	public SaveFilePage getSaveFilePage() {
		return saveFilePage;
	}
}

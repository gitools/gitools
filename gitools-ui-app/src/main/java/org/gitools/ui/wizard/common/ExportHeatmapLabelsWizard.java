/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.ui.wizard.common;

import org.gitools.heatmap.Heatmap;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;

public class ExportHeatmapLabelsWizard extends AbstractWizard {

	private FileFormat[] supportedFormats = new FileFormat[] {
		FileFormats.TEXT
	};

	private Heatmap hm;

	private ExportHeatmapLabelsPage sourcePage;
	private PatternSourcePage patPage;
	private SaveFilePage savePage;

	public ExportHeatmapLabelsWizard(Heatmap hm) {
		this.hm = hm;

		setTitle("Export labels ...");
	}

	@Override
	public void addPages() {
		sourcePage = new ExportHeatmapLabelsPage();
		addPage(sourcePage);

		patPage = new PatternSourcePage(true);
		addPage(patPage);
		
		savePage = new SaveFilePage();
		savePage.setTitle("Select destination file");
		savePage.setFolder(Settings.getDefault().getLastExportPath());
		savePage.setFormats(supportedFormats);
		addPage(savePage);
	}

	@Override
	public void performFinish() {
		Settings.getDefault().setLastExportPath(savePage.getFolder());
		Settings.getDefault().save();
	}

	@Override
	public void pageLeft(IWizardPage page) {
		if (page == sourcePage) {
			switch (sourcePage.getWhichLabels()) {
				case VISIBLE_ROWS:
				case HIDDEN_ROWS:
					patPage.setAnnotationMatrix(hm.getRowDim().getAnnotations());
					break;

				case VISIBLE_COLUMNS:
				case HIDDEN_COLUMNS:
					patPage.setAnnotationMatrix(hm.getColumnDim().getAnnotations());
					break;
			}
		}
	}

	public ExportHeatmapLabelsPage.WhichLabels getWhichLabels() {
		return sourcePage.getWhichLabels();
	}

	public String getPattern() {
		return patPage.getPattern();
	}

	public SaveFilePage getSavePage() {
		return savePage;
	}

}

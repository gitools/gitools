/*
 *  Copyright 2010 cperez.
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

package org.gitools.ui.common.wizard;

import java.io.File;
import org.gitools.persistence.FileFormat;
import org.gitools.ui.platform.wizard.AbstractWizard;


public class SaveFileWizard extends AbstractWizard {

	private SaveFilePage page;

	public SaveFileWizard() {
		setTitle("Select destination file");
	}

	@Override
	public void addPages() {
		addPage(getSaveFilePage());
	}

	public SaveFilePage getSaveFilePage() {
		if (page == null)
			page = new SaveFilePage();
		return page;
	}

	public String getFileName() {
		return getSaveFilePage().getFileName();
	}

	public String getFilePath() {
		return getSaveFilePage().getFilePath();
	}

	public File getFile() {
		return getSaveFilePage().getFile();
	}

	public String getFolder() {
		return getSaveFilePage().getFolder();
	}

	public FileFormat getFormat() {
		return getSaveFilePage().getFormat();
	}
	
	public static SaveFileWizard createSimple(
			String title, String fileName,
			String folder, FileFormat[] fileFormats) {

		SaveFileWizard wiz = new SaveFileWizard();
		wiz.setTitle(title);

		SaveFilePage page = wiz.getSaveFilePage();
		page.setTitle("Select destination file");
		page.setFileName(fileName);
		page.setFolder(folder);
		page.setFormats(fileFormats);
		page.setFormatsVisible(fileFormats.length > 1);
		return wiz;
	}
}

/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.app.wizard.common;

import org.gitools.analysis._DEPRECATED.formats.FileFormat;
import org.gitools.ui.platform.wizard.AbstractWizard;

import java.io.File;


public class SaveFileWizard extends AbstractWizard {

    private SaveFilePage page;

    private SaveFileWizard() {
        setTitle("Select destination file");
    }

    @Override
    public void addPages() {
        addPage(getSaveFilePage());
    }

    SaveFilePage getSaveFilePage() {
        if (page == null) {
            page = new SaveFilePage();
        }
        return page;
    }

    public String getFileNameWithoutExt() {
        return getSaveFilePage().getFileNameWithoutExtension();
    }


    public String getFileName() {
        return getSaveFilePage().getFileName();
    }


    public File getPathAsFile() {
        return getSaveFilePage().getPathAsFile();
    }

    public String getFolder() {
        return getSaveFilePage().getFolder();
    }


    public FileFormat getFormat() {
        return getSaveFilePage().getFormat();
    }


    public static SaveFileWizard createSimple(String title, String fileName, String folder, FileFormat fileFormat) {
        return createSimple(title, fileName, folder, new FileFormat[]{fileFormat});
    }


    public static SaveFileWizard createSimple(String title, String fileName, String folder, FileFormat[] fileFormats) {

        SaveFileWizard wiz = new SaveFileWizard();
        wiz.setTitle(title);

        SaveFilePage page = wiz.getSaveFilePage();
        page.setTitle("Select destination file");
        page.setFileNameWithoutExtension(fileName);
        page.setFolder(folder);
        page.setFormats(fileFormats);
        page.setFormatsVisible(fileFormats.length > 1);
        return wiz;
    }
}

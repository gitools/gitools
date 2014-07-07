/*
 * #%L
 * org.gitools.ui.core
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.core.pages.common;

import org.gitools.api.persistence.FileFormat;
import org.gitools.ui.platform.wizard.IWizardPage;

import java.io.File;

/**
 * Created by mschroeder on 5/29/14.
 */
public interface SaveFilePage extends IWizardPage {


    void updateGeneratedFile();

    String getFileNameWithoutExtension();

    void setFileNameWithoutExtension(String name);

    String getFolder();

    void setFolder(String folderPath);

    void setFormats(FileFormat[] formats);

    FileFormat getFormat();

    void setFormatsVisible(boolean visible);

    /* Returns the file name with extension appended */
    String getFileName();

    File getPathAsFile();
}

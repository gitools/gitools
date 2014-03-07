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
package org.gitools.ui.app.utils;

import com.jidesoft.swing.FolderChooser;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.Application;

import java.awt.*;
import java.io.File;

public class FileChooserUtils {

    public static final int MODE_SAVE = 1;
    public static final int MODE_OPEN = 2;

    public static FileChoose selectFile(String title, String currentPath, int mode) {
        return selectFile(title, currentPath, mode, null);
    }

    public static FileChoose selectFile(String title, int mode, FileFormatFilter[] filters) {
        return selectFile(title, Settings.getDefault().getLastPath(), mode, filters);
    }

    /**
     * Select a file taking into account a set of file filters.
     *
     * @param title
     * @param currentPath
     * @param mode
     * @param filters
     * @return {file, filter}
     */
    public static FileChoose selectFile(String title, String currentPath, int mode, FileFormatFilter[] filters) {
        return selectFileAWT(title, currentPath, mode, filters);
    }

    private static FileChoose selectFileAWT(String title, String currentPath, int mode, FileFormatFilter[] filters) {

        FileDialog dialog = new java.awt.FileDialog((java.awt.Frame) null, title, (mode == MODE_OPEN ? FileDialog.LOAD : FileDialog.SAVE));
        dialog.setDirectory(currentPath);
        dialog.setMultipleMode(false);
        dialog.setVisible(true);

        String file = dialog.getFile();
        return (file == null ? null : new FileChoose(new File(dialog.getDirectory(), dialog.getFile()), null));

    }

    public static File selectPath(String title, String currentPath) {
        return selectPathJIDE(title, currentPath);
    }

    private static File selectPathJIDE(String title, String currentPath) {

        FolderChooser chooser = new FolderChooser(currentPath);
        chooser.setDialogTitle(title);
        chooser.setRecentListVisible(false);

        int returnVal = chooser.showOpenDialog(Application.get());
        if(returnVal == FolderChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }


    private static String getExtension(String fileName) {
        String ext = null;
        int i = fileName.lastIndexOf('.');

        if (i > 0 && i < fileName.length() - 1) {
            ext = fileName.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}

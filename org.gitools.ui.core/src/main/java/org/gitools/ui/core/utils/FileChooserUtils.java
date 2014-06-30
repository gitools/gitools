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
package org.gitools.ui.core.utils;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.utils.swing.DialogOptions;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.settings.Settings;

import java.awt.*;
import java.io.File;

public class FileChooserUtils {

    public static final int MODE_SAVE = 1;
    public static final int MODE_OPEN = 2;

    public static FileChoose selectFile(String title, String currentPath, int mode) {
        return selectFile(title, currentPath, mode, null);
    }

    public static FileChoose selectFile(String title, int mode, FileFormatFilter[] filters) {
        return selectFile(title, Settings.get().getLastPath(), mode, filters);
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
        return selectFile(title, currentPath, null, mode, filters);
    }

    public static FileChoose selectFile(String title, String currentPath, String fileName, int mode) {
        return selectFileAWT(title, currentPath, fileName, mode);
    }

    public static FileChoose selectFile(String title, String currentPath, String fileName, int mode, FileFormatFilter[] filters) {
        return selectFileAWT(title, currentPath, fileName, mode);
    }

    private static FileChoose selectFileAWT(String title, String currentPath, String fileName, int mode) {

        final FileDialog dialog = new java.awt.FileDialog(Application.get(), title, (mode == MODE_OPEN ? FileDialog.LOAD : FileDialog.SAVE));
        dialog.setDirectory(currentPath);
        dialog.setMultipleMode(false);
        dialog.setAlwaysOnTop(true);
        dialog.requestFocus();
        dialog.toFront();
        dialog.repaint();
        dialog.setVisible(true);

        if (fileName != null) {
            dialog.setFile(fileName);
        }

        String file = dialog.getFile();
        return (file == null ? null : new FileChoose(new File(dialog.getDirectory(), dialog.getFile()), null));

    }

    public static File selectPath(String title, String currentPath) {
        return selectPathWeb(title, currentPath);
    }


    private static File selectPathWeb(String title, String currentPath) {

        WebDirectoryChooser chooser = new WebDirectoryChooser(Application.get(), title);

        File selectedDirectory = new File(currentPath);
        if (selectedDirectory.exists() && selectedDirectory.isDirectory()) {
            chooser.setSelectedDirectory(selectedDirectory);
        }

        int returnVal = chooser.showDialog();
        if(returnVal == DialogOptions.OK_OPTION) {
            return chooser.getSelectedDirectory();
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

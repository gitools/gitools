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

import com.googlecode.vfsjfilechooser2.VFSJFileChooser;
import com.googlecode.vfsjfilechooser2.filechooser.AbstractVFSFileFilter;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.Application;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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

    private static FileChoose selectFileSwing(String title, String currentPath, int mode, FileFormatFilter[] filters) {

        JFileChooser fileChooser = new JFileChooser(currentPath);
        fileChooser.setFileHidingEnabled(true);

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (filters != null) {
            fileChooser.setAcceptAllFileFilterUsed(false);
            for (FileFormatFilter filter : filters)
                fileChooser.addChoosableFileFilter(filter);

            if (filters.length > 0) {
                fileChooser.setFileFilter(filters[0]);
            }
        }

        int retval = JFileChooser.CANCEL_OPTION;

        if (mode == FileChooserUtils.MODE_SAVE) {
            retval = fileChooser.showSaveDialog(Application.get());
        } else if (mode == FileChooserUtils.MODE_OPEN) {
            retval = fileChooser.showOpenDialog(Application.get());
        }

        if (retval == JFileChooser.APPROVE_OPTION) {
            FileFormatFilter formatFilter = (FileFormatFilter) fileChooser.getFileFilter();
            return new FileChoose(fileChooser.getSelectedFile(), formatFilter);
        }

        return null;
    }

    public static File selectPath(String title, String currentPath) {
        return selectPathVFS(title, currentPath);
    }

    private static File selectPathSwing(String title, String currentPath) {

        JFileChooser fileChooser = new JFileChooser(currentPath)
        {
            public void approveSelection()
            {
                if (getSelectedFile().isFile())
                {
                    // beep
                    return;
                }
                else
                    super.approveSelection();
            }
        };
        fileChooser.setFileHidingEnabled(true);

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Folders";
            }
        });

        int retval = fileChooser.showOpenDialog(Application.get());
        if (retval == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private static File selectPathVFS(String title, String currentPath) {
        VFSJFileChooser fileChooser = new VFSJFileChooser(currentPath);
        fileChooser.setFileHidingEnabled(false);

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(VFSJFileChooser.SELECTION_MODE.DIRECTORIES_ONLY);
        fileChooser.setPreferredSize(new Dimension(640, 480));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new AbstractVFSFileFilter() {
            @Override
            public boolean accept(FileObject f) {
                try {
                    return f.getType() == FileType.FOLDER;
                } catch (FileSystemException e) {
                    return true;
                }
            }

            @Override
            public String getDescription() {
                return "Folders";
            }
        });

        VFSJFileChooser.RETURN_TYPE retval = fileChooser.showOpenDialog(Application.get());
        if (retval == VFSJFileChooser.RETURN_TYPE.APPROVE) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    /*private static FileChoose selectFileVFS(String title, String currentPath, int mode, FileFormatFilter[] filters) {
        *//*TODO
        try
        {
            FileSystemOptions opts = new FileSystemOptions();
            FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
        } catch (FileSystemException e)
        {
            throw new RuntimeException(e);
        } *//*

        VFSJFileChooser fileChooser = new VFSJFileChooser(currentPath);
        fileChooser.setFileHidingEnabled(false);

        //TODO Fix VFS integration
        //fileChooser.setAccessory(new DefaultAccessoriesPanel(fileChooser));

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(VFSJFileChooser.SELECTION_MODE.FILES_ONLY);
        fileChooser.setPreferredSize(new Dimension(640, 480));

        if (filters != null) {
            fileChooser.setAcceptAllFileFilterUsed(false);
            for (FileFormatFilter filter : filters)
                fileChooser.addChoosableFileFilter(new VFSFileFilterAdaptor(filter));

            if (filters.length > 0) {
                fileChooser.setFileFilter(new VFSFileFilterAdaptor(filters[0]));
            }
        }

        VFSJFileChooser.RETURN_TYPE retval = VFSJFileChooser.RETURN_TYPE.CANCEL;

        if (mode == FileChooserUtils.MODE_SAVE) {
            retval = fileChooser.showSaveDialog(Application.get());
        } else if (mode == FileChooserUtils.MODE_OPEN) {
            retval = fileChooser.showOpenDialog(Application.get());
        }

        if (retval == VFSJFileChooser.RETURN_TYPE.APPROVE) {

            FileFormatFilter formatFilter = null;
            if (fileChooser.getFileFilter() instanceof VFSFileFilterAdaptor) {
                formatFilter = ((VFSFileFilterAdaptor) fileChooser.getFileFilter()).getFilter();
            }

            return new FileChoose(fileChooser.getSelectedFile(), formatFilter);
        }

        return null;
    }*/


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

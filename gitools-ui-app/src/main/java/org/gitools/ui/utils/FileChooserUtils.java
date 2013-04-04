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
package org.gitools.ui.utils;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class FileChooserUtils
{

    public static final int MODE_SAVE = 1;
    public static final int MODE_OPEN = 2;

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    public final static String pdf = "pdf";

    public static class FileAndFilter
    {
        private File file;
        private FileFilter filter;

        public FileAndFilter(File file, FileFilter filter)
        {
            this.file = file;
            this.filter = filter;
        }

        public File getFile()
        {
            return file;
        }

        public FileFilter getFilter()
        {
            return filter;
        }
    }

    @NotNull
    private static FileFilter imageFileFilter = new FileFilter()
    {
        @Override
        public boolean accept(@NotNull File f)
        {
            if (f.isDirectory())
            {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null)
            {
                return isImageExtension(extension);
            }

            return false;
        }

        @NotNull
        @Override
        public String getDescription()
        {
            return "Image files (*.png, *.jpg, *.jpeg, *.gif)";
        }
    };

    @NotNull
    private static FileFilter pdfFileFilter = new FileFilter()
    {
        @Override
        public boolean accept(@NotNull File f)
        {
            if (f.isDirectory())
            {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null)
            {
                if (extension.equals(pdf))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            return false;
        }

        @NotNull
        @Override
        public String getDescription()
        {
            return "Image files (*.pdf)";
        }
    };

    @Nullable
    public static File selectFile(String title, int mode)
    {
        return selectFile(title, Settings.getDefault().getLastPath(), mode);
    }

    @Nullable
    public static File selectFile(String title, String currentPath, int mode)
    {
        JFileChooser fileChooser = new JSystemFileChooser(currentPath);

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setPreferredSize(new Dimension(640, 480));

        int retval = JFileChooser.CANCEL_OPTION;

        if (mode == FileChooserUtils.MODE_SAVE)
        {
            retval = fileChooser.showSaveDialog(AppFrame.get());
        }
        else if (mode == FileChooserUtils.MODE_OPEN)
        {
            retval = fileChooser.showOpenDialog(AppFrame.get());
        }

        if (retval == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            return file;
        }

        return null;
    }

    @Nullable
    public static FileAndFilter selectFile(String title, int mode, FileFilter[] filters)
    {
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
    @Nullable
    public static FileAndFilter selectFile(String title, String currentPath, int mode, @Nullable FileFilter[] filters)
    {
        JFileChooser fileChooser = new JSystemFileChooser(currentPath);

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setPreferredSize(new Dimension(640, 480));

        if (filters != null)
        {
            fileChooser.setAcceptAllFileFilterUsed(false);
            for (FileFilter filter : filters)
                fileChooser.addChoosableFileFilter(filter);

            if (filters.length > 0)
            {
                fileChooser.setFileFilter(filters[0]);
            }
        }

        int retval = JFileChooser.CANCEL_OPTION;

        if (mode == FileChooserUtils.MODE_SAVE)
        {
            retval = fileChooser.showSaveDialog(AppFrame.get());
        }
        else if (mode == FileChooserUtils.MODE_OPEN)
        {
            retval = fileChooser.showOpenDialog(AppFrame.get());
        }

        if (retval == JFileChooser.APPROVE_OPTION)
        {
            return new FileAndFilter(
                    fileChooser.getSelectedFile(),
                    fileChooser.getFileFilter());
        }

        return null;
    }

    @Nullable
    public static File selectPath(String title)
    {
        return selectPath(title, Settings.getDefault().getLastPath());
    }

    @Nullable
    public static File selectPath(String title, String currentPath)
    {
        JFileChooser fileChooser = new JSystemFileChooser(currentPath);

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setPreferredSize(new Dimension(640, 480));

        int retval = fileChooser.showOpenDialog(AppFrame.get());
        if (retval == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            return file;
        }

        return null;
    }

    @Nullable
    public static File selectImageFile(String title, String currentPath, int mode)
    {
        JFileChooser fileChooser = new JSystemFileChooser(currentPath);

        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(imageFileFilter);
        fileChooser.setPreferredSize(new Dimension(640, 480));

        int retval = JFileChooser.CANCEL_OPTION;

        if (mode == FileChooserUtils.MODE_SAVE)
        {
            retval = fileChooser.showSaveDialog(AppFrame.get());
        }
        else if (mode == FileChooserUtils.MODE_OPEN)
        {
            retval = fileChooser.showOpenDialog(AppFrame.get());
        }

        if (retval == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            return file;
        }

        return null;
    }

    @Nullable
    public static String getExtension(@NotNull File file)
    {
        return getExtension(file.getName());
    }

    @Nullable
    public static String getExtension(@NotNull String fileName)
    {
        String ext = null;
        int i = fileName.lastIndexOf('.');

        if (i > 0 && i < fileName.length() - 1)
        {
            ext = fileName.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static boolean isImageExtension(@NotNull String extension)
    {
        return extension.equals(tif)
                || extension.equals(gif)
                || extension.equals(jpeg)
                || extension.equals(jpg)
                || extension.equals(png);
    }
}

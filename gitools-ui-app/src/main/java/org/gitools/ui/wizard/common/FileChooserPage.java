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
package org.gitools.ui.wizard.common;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class FileChooserPage extends AbstractWizardPage
{

    private int selectionMode;
    private File currentPath;
    private File selectedFile;
    private FileFilter fileFilter;

    private JFileChooser fileChooser;

    public FileChooserPage()
    {
        this(JFileChooser.FILES_ONLY);
    }

    public FileChooserPage(int selectionMode)
    {
        this.selectionMode = selectionMode;
    }

    @Override
    public JComponent createControls()
    {

        fileChooser = new JFileChooser();
        fileChooser.setControlButtonsAreShown(false);
        fileChooser.setFileSelectionMode(selectionMode);
        fileChooser.setFileFilter(fileFilter);

        if (currentPath != null)
        {
            fileChooser.setCurrentDirectory(currentPath);
        }
        if (selectedFile != null)
        {
            fileChooser.setSelectedFile(selectedFile);
        }
        updateComplete();

        fileChooser.addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())
                        || JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(evt.getPropertyName()))
                {
                    updateComplete();
                }
            }
        });

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(fileChooser, BorderLayout.CENTER);
        return p;
    }

    private void updateComplete()
    {
        setComplete(fileChooser.getSelectedFile() != null
                || fileChooser.getSelectedFiles().length > 0);
    }

    public void setFileSelectionMode(int selectionMode)
    {
        this.selectionMode = selectionMode;
    }

    public void setCurrentPath(File file)
    {
        currentPath = file;
    }

    public void setSelectedFile(File file)
    {
        selectedFile = file;
    }

    public File getSelectedFile()
    {
        return fileChooser.getSelectedFile();
    }

    public File[] getSelectedFiles()
    {
        return fileChooser.getSelectedFiles();
    }

    public FileFilter getFileFilter()
    {
        return fileFilter;
    }

    public void setFileFilter(FileFilter fileFilter)
    {
        this.fileFilter = fileFilter;
    }
}

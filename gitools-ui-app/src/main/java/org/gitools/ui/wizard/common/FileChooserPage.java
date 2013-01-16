/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class FileChooserPage extends AbstractWizardPage {

	private int selectionMode;
	private File currentPath;
	private File selectedFile;
    private FileFilter fileFilter;
	
	private JFileChooser fileChooser;
	
	public FileChooserPage() {
		this(JFileChooser.FILES_ONLY);
	}
	
	public FileChooserPage(int selectionMode) {
		this.selectionMode = selectionMode;
	}

	@Override
	public JComponent createControls() {

		fileChooser = new JFileChooser();
		fileChooser.setControlButtonsAreShown(false);
		fileChooser.setFileSelectionMode(selectionMode);
        fileChooser.setFileFilter(fileFilter);
		
		if (currentPath != null)
			fileChooser.setCurrentDirectory(currentPath);
		if (selectedFile != null)
			fileChooser.setSelectedFile(selectedFile);
		updateComplete();
		
		fileChooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())
					|| JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(evt.getPropertyName()))
					updateComplete();
			}
		});

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(fileChooser, BorderLayout.CENTER);
		return p;
	}
	
	private void updateComplete() {
		setComplete(fileChooser.getSelectedFile() != null
				|| fileChooser.getSelectedFiles().length > 0);
	}
	
	public void setFileSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
	}
	
	public void setCurrentPath(File file) {
		currentPath = file;
	}
	
	public void setSelectedFile(File file) {
		selectedFile = file;
	}
	
	public File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}
	
	public File[] getSelectedFiles() {
		return fileChooser.getSelectedFiles();
	}

    public FileFilter getFileFilter() {
        return fileFilter;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }
}

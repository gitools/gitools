package org.gitools.ui.common.wizard;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class FileChooserPage extends AbstractWizardPage {

	private int selectionMode;
	private File currentPath;
	private File selectedFile;
	
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
}

package org.gitools.ui.wizard.common;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.gitools.ui.wizard.AbstractWizardPage;

public class FileChooserPage extends AbstractWizardPage {

	private String title;
	private int selectionMode;
	
	private JFileChooser fileChooser;
	
	public FileChooserPage() {
		this("Select file", JFileChooser.FILES_ONLY);
	}
	
	public FileChooserPage(String title) {
		this(title, JFileChooser.FILES_ONLY);
	}
	
	public FileChooserPage(String title, int selectionMode) {
		this.title = title;
		this.selectionMode = selectionMode;
	}

	@Override
	public JComponent createControls() {

		setTitle(title);
		
		fileChooser = new JFileChooser();
		fileChooser.setControlButtonsAreShown(false);
		fileChooser.setFileSelectionMode(selectionMode);
		fileChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
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
	
	public File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}
	
	public File[] getSelectedFiles() {
		return fileChooser.getSelectedFiles();
	}
}

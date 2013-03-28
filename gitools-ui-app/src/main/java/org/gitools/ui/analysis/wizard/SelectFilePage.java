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

/*
 * DataSourcePanel.java
 *
 * Created on September 4, 2009, 1:58 PM
 */

package org.gitools.ui.analysis.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileFilter;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.utils.FileFormatFilter;

public class SelectFilePage extends AbstractWizardPage {

	private static final long serialVersionUID = 3840797252370672587L;

	private static final FileFormat anyFileFormat = new FileFormat("Any file format", "", "", false, false);

	private static final FileFormat[] defaultFormats = new FileFormat[] { anyFileFormat };

	private FileFormat[] formats;
	private boolean blankFileAllowed;
	private String lastPath;

    public SelectFilePage(FileFormat[] formats) {
		setTitle("Select file");

		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_SELECT_FILE, 96));
		
        initComponents();

		this.formats = formats != null ? formats : defaultFormats;
		blankFileAllowed = false;

		formatCb.setModel(new DefaultComboBoxModel(formats));
		formatCb.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				updateState(); }
		});

		DocumentChangeListener docCompleteListener = new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				updateState(); }
		};

                valueCb.setVisible(false);
                valueLabel.setVisible(false);

		filePath.getDocument().addDocumentListener(docCompleteListener);
    }

	protected void updateState() {
		FileFormat ff = getFileFormat();

		setMessage(MessageStatus.INFO, "");

		boolean complete = true;

		String path = filePath.getText().trim().toLowerCase();
		if (!path.isEmpty()) {
			if (!ff.checkExtension(path))
				setMessage(MessageStatus.WARN, "The file extension doesn't match the selected format");
		}

		complete = blankFileAllowed || !filePath.getText().isEmpty();

		path = filePath.getText();
		if (!path.isEmpty()) {
			File rowsFilterFile = new File(path);
			if (!rowsFilterFile.exists()) {
				//complete = false;
				setMessage(MessageStatus.WARN, "File not found: " + path);
			}
		}

		setComplete(complete);
	}

	@Override
	public JComponent createControls() {
		return this;
	}

	public void setBlankFileAllowed(boolean allowed) {
		this.blankFileAllowed = allowed;
		updateState();
	}

	protected String getLastPath() {
		if (lastPath == null)
			lastPath = new File(".").getAbsolutePath();
		return lastPath;
	}

	protected void setLastPath(String path) {
		this.lastPath = path;
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        formatCb = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        filePath = new javax.swing.JTextField();
        fileBrowseBtn = new javax.swing.JButton();
        valueLabel = new javax.swing.JLabel();
        valueCb = new javax.swing.JComboBox();

        jLabel1.setText("Format");

        formatCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Binary data matrix", "Continuous data matrix" }));

        jLabel2.setText("File");

        fileBrowseBtn.setText("Browse...");
        fileBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseBtnActionPerformed(evt);
            }
        });

        valueLabel.setText("Value");
        valueLabel.setEnabled(false);

        valueCb.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Value1", "Value2"}));
        valueCb.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formatCb, 0, 576, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filePath, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileBrowseBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(valueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valueCb, 0, 586, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(formatCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileBrowseBtn)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valueLabel)
                    .addComponent(valueCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(238, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void fileBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseBtnActionPerformed
		boolean anyFormat = formats.length == 1 && formats[0] == anyFileFormat;

		FileFilter any = new FileFilter() {
			@Override public boolean accept(File pathname) {
				return true; }
			@Override public String getDescription() {
				return anyFileFormat.getTitle(); }
		};

		FileFilter[] filters = null;
		if (anyFormat)
			filters = new FileFilter[] { any };
		else {
			filters = new FileFilter[formats.length + 2];
			filters[0] = any;
			filters[1] = new FileFormatFilter("Known formats", null, formats);
			for (int i = 0; i < formats.length; i++)
				filters[i + 2] = new FileFormatFilter(formats[i]);
		}

		FileChooserUtils.FileAndFilter sel = FileChooserUtils.selectFile(
				"Select file",
				getLastPath(),
				FileChooserUtils.MODE_OPEN,
				filters);

		if (sel != null) {
			File selPath = sel.getFile();
			//FileFilter filt = sel.getFilter();

			/*if (filt == filters[0] || filt == filters[1]) {
				String fileName = selPath.getName();
				for (FileFormat f : formats)
					if (f.checkExtension(fileName)) {
						formatCb.setSelectedItem(f);
						break;
					}
			}
			else {
				FileFormat ff = ((FileFormatFilter) filt).getFormat();
				formatCb.setSelectedItem(ff);
			}*/

			setFile(selPath);

			/*String fileName = selPath.getName().toLowerCase();
			for (FileFormat ff : formats) {
				if (ff.checkExtension(fileName)) {
					formatCb.setSelectedItem(ff);
					break;
				}
			}*/
			
			//filePath.setText(selPath.getAbsolutePath());
			setLastPath(selPath.getAbsolutePath());
		}
	}//GEN-LAST:event_fileBrowseBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton fileBrowseBtn;
    private javax.swing.JTextField filePath;
    private javax.swing.JComboBox formatCb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox valueCb;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables

	public FileFormat getFileFormat() {
		return (FileFormat) formatCb.getSelectedItem();
	}

	public File getFile() {
		String path = filePath.getText();
		return path.isEmpty() ? null : new File(path);
	}

	public void setFile(File file) {
		String fileName = file.getName();
		for (FileFormat f : formats)
			if (f.checkExtension(fileName)) {
				formatCb.setSelectedItem(f);
				break;
			}
		
		filePath.setText(file.getAbsolutePath());

		updateState();
	}

    public void activateValueSelection() {
            valueCb.setVisible(true);
            valueLabel.setVisible(true);
            valueCb.setEnabled(true);
            valueLabel.setEnabled(true);
        }

    public void deactivateValueSelection() {
            valueCb.setEnabled(false);
            valueLabel.setEnabled(false);
            valueCb.setVisible(false);
            valueLabel.setVisible(false);
        }

    protected void setValues (String[] values) {
        valueCb.setModel(new DefaultComboBoxModel(values));
    }
    
    public int getSelectedValueIndex () {
        if (valueCb.isEnabled() == true)
            return valueCb.getSelectedIndex();
        else
            return -1;  /* return -1 if not enabled */
    }
    
    public String[] getValues () {
        //if (valueCb.isEnabled() == true)
            //TODO: return items
        return new String[0];
    }
}

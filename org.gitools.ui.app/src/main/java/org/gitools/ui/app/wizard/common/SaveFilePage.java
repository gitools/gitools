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
package org.gitools.ui.app.wizard.common;

import org.gitools.analysis._DEPRECATED.formats.FileFormat;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.app.utils.DocumentChangeListener;
import org.gitools.ui.app.utils.FileChooserUtils;
import org.gitools.ui.app.utils.FileFormatFilter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveFilePage extends AbstractWizardPage {

    private FileFormat[] formats;

    /**
     * Creates new form SaveFilePage
     */
    public SaveFilePage() {
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_SAVE, 96));

        initComponents();

        fileName.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                setComplete(!fileName.getText().isEmpty());
                updateGeneratedFile();
            }
        });

        folder.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                updateGeneratedFile();
            }
        });

        format.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGeneratedFile();
            }
        });
    }

    private void updateGeneratedFile() {
        File file = getPathAsFile();
        String fn = file.getAbsolutePath();
        path.setText(fn);
        if (file.exists()) {
            setMessage(MessageStatus.WARN, "File " + file.getName() + " already exists");
            setComplete(false);
        } else {
            setMessage(MessageStatus.INFO, "");
        }

        FileFormat fmt = getFormat();
        if (formats != null && formats.length > 1 && fmt != null && !fmt.checkExtension(fn)) {
            setMessage(MessageStatus.WARN, "The file extension doesn't match the selected format");
        }
    }


    @Override
    public JComponent createControls() {
        return this;
    }

    /**
     * Return only the file name
     */
    public String getFileNameWithoutExtension() {
        return fileName.getText();
    }

    public void setFileNameWithoutExtension(String name) {
        fileName.setText(name);
    }

    /**
     * Return only the folder
     */
    public String getFolder() {
        return folder.getText();
    }

    public void setFolder(String folderPath) {
        folder.setText(folderPath);
    }

    public void setFormats(FileFormat[] formats) {
        this.formats = formats;
        format.setModel(new DefaultComboBoxModel(formats));
        updateGeneratedFile();
    }


    public FileFormat getFormat() {
        return (FileFormat) format.getSelectedItem();
    }

    public void setFormatsVisible(boolean visible) {
        formatLabel.setVisible(visible);
        format.setVisible(visible);
    }

    /* Returns the file name with extension appended */

    public String getFileName() {
        StringBuilder sb = new StringBuilder();
        String name = getFileNameWithoutExtension();
        sb.append(name);

        if (!name.isEmpty() && format.getSelectedIndex() >= 0) {
            FileFormat fmt = (FileFormat) format.getSelectedItem();
            if (!name.endsWith("." + fmt.getExtension())) {
                if (!name.endsWith(".")) {
                    sb.append('.');
                }
                sb.append(fmt.getExtension());
            }
        }

        return sb.toString();
    }

    /**
     * Returns the full path as a file
     */

    public File getPathAsFile() {
        String folderName = folder.getText();
        if (folderName.isEmpty()) {
            folderName = System.getProperty("user.dir");
        }

        return new File(folderName, getFileName());
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        fileName = new javax.swing.JTextField();
        browseFolderBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        folder = new javax.swing.JTextField();
        formatLabel = new javax.swing.JLabel();
        format = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        path = new javax.swing.JTextField();
        browseFileBtn = new javax.swing.JButton();

        jLabel1.setText("Name");

        fileName.setFocusCycleRoot(true);

        browseFolderBtn.setText("Browse...");
        browseFolderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseFolderBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Folder");

        formatLabel.setText("Format");

        jLabel4.setText("Generated file");

        path.setEditable(false);

        browseFileBtn.setText("Browse...");
        browseFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseFileBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(formatLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(format, 0, 396, Short.MAX_VALUE).addComponent(fileName, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE).addComponent(folder, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(browseFolderBtn).addComponent(browseFileBtn))).addGroup(layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(path, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(fileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(browseFileBtn)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(folder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(browseFolderBtn)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(formatLabel).addComponent(format, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(241, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void browseFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseFolderBtnActionPerformed
        File selPath = FileChooserUtils.selectPath("Select folder", folder.getText());

        if (selPath != null) {
            folder.setText(selPath.getAbsolutePath());
        }
    }//GEN-LAST:event_browseFolderBtnActionPerformed

    private void browseFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseFileBtnActionPerformed
        FileFormatFilter ff = new FileFormatFilter("Known files") {
            @Override
            public boolean accept(boolean directory, String fileName) {
                if (directory) {
                    return true;
                }

                for (FileFormat fmt : formats)
                    if (fmt.checkExtension(fileName)) {
                        return true;
                    }

                return false;
            }


            @Override
            public String getDescription() {
                StringBuilder sb = new StringBuilder();
                sb.append("Supported formats");
                if (formats != null && formats.length > 0) {
                    sb.append(" (*.").append(formats[0].getExtension());
                    for (int i = 1; i < formats.length; i++)
                        sb.append(", *.").append(formats[i].getExtension());
                    sb.append(')');
                }
                return sb.toString();
            }
        };

        File selFile = FileChooserUtils.selectFile("Select file", folder.getText(), FileChooserUtils.MODE_OPEN, new FileFormatFilter[]{ff}).getFile();

        if (selFile != null) {
            String fn = selFile.getName();
            fileName.setText(fn);
            folder.setText(selFile.getParentFile().getAbsolutePath());
            for (FileFormat f : formats)
                if (f.checkExtension(fn)) {
                    format.setSelectedItem(f);
                }
        }
    }//GEN-LAST:event_browseFileBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseFileBtn;
    private javax.swing.JButton browseFolderBtn;
    private javax.swing.JTextField fileName;
    private javax.swing.JTextField folder;
    private javax.swing.JComboBox format;
    private javax.swing.JLabel formatLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField path;
    // End of variables declaration//GEN-END:variables

}

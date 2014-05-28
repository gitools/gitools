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
package org.gitools.ui.core.pages.common;

import org.gitools.api.persistence.FileFormat;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.core.utils.FileChooserUtils;
import org.gitools.ui.core.utils.FileFormatFilter;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.icons.IconNames;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;

public class SaveHeatmapFilePage extends SaveFilePage {

    private static Pattern VALID_FILENAME_CHARACTER = Pattern.compile("[^A-Za-z0-9_\\-]");
    private FileFormat[] formats;
    private JButton browseFileBtn;
    private JButton browseFolderBtn;
    private JTextField fileName;
    private JTextField folder;
    private JComboBox format;
    private JLabel formatLabel;
    private JTextField path;
    private JPanel rootPanel;
    private JCheckBox discardHidden;
    private JPanel dataOptionsPanel;
    private JCheckBox optimizeData;

    /**
     * Creates new form SaveFilePageOLDOLDOLD
     */
    public SaveHeatmapFilePage() {
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
            setMessage(MessageStatus.WARN, "File " + file.getName() + " already exists and will be overwritten");
            setComplete(true);
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
        return rootPanel;
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

        // Filter invalid characters
        name = VALID_FILENAME_CHARACTER.matcher(name).replaceAll("_");

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

    private void initComponents() {


        fileName.setFocusCycleRoot(true);

        browseFolderBtn.setText("Browse...");
        browseFolderBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                browseFolderBtnActionPerformed(evt);
            }
        });

        path.setEditable(false);

        browseFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                browseFileBtnActionPerformed(evt);
            }
        });
    }

    private void browseFolderBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_browseFolderBtnActionPerformed
        File selPath = FileChooserUtils.selectPath("Select folder", folder.getText());

        if (selPath != null) {
            folder.setText(selPath.getAbsolutePath());
        }
    }

    private void browseFileBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_browseFileBtnActionPerformed
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

        File selFile = FileChooserUtils.selectFile("Select file", folder.getText(), FileChooserUtils.MODE_SAVE, new FileFormatFilter[]{ff}).getFile();

        if (selFile != null) {
            String fn = selFile.getName();
            fileName.setText(fn);
            folder.setText(selFile.getParentFile().getAbsolutePath());
            for (FileFormat f : formats)
                if (f.checkExtension(fn)) {
                    format.setSelectedItem(f);
                }
        }
    }

    public boolean isDiscardHidden() {
        return discardHidden.isSelected();
    }

    public void enableDiscardHidden(boolean b) {
        discardHidden.setEnabled(b);
    }

    public boolean isOptimizeData() {
        return optimizeData.isSelected();
    }

    public void enabledOptimizeData(boolean b) {
        optimizeData.setEnabled(b);
    }

    public void suggestDiscardHidden() {
        setMessage(MessageStatus.INFO, "Discard hidden data to improve file performance (big data files).");
    }

}

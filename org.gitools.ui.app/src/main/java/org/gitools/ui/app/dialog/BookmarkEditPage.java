/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.dialog;

import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Bookmarks;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.CloneUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BookmarkEditPage extends AbstractWizardPage {
    private final Bookmarks existingBookmarks;
    private JPanel panel;
    private JTextField nameField;
    private JLabel rowNbLabel;
    private JLabel colNbLabel;
    private JComboBox dataLayerComboBox;
    private JCheckBox noLayerCheckBox;
    private JButton deleteThisBookmarkButton;
    private JCheckBox noColumnsCheckBox;
    private JCheckBox noRowsCheckBox;
    private boolean delete = false;
    private boolean creating;
    private List<String> forbiddenNames;
    private Bookmark bookmark;
    private Bookmark backup;

    public Bookmark getBookmark() {
        return bookmark;
    }


    public BookmarkEditPage(Heatmap heatmap, final Bookmark bookmark, boolean creating) {
        this.bookmark = CloneUtils.clone(bookmark);
        this.backup = bookmark;
        this.existingBookmarks = heatmap.getBookmarks();
        this.creating = creating;
        this.forbiddenNames = new ArrayList<>();
        for (Bookmark b : existingBookmarks.getAll()) {
            forbiddenNames.add(b.getName());
        }

        // DATA LAYER

        noLayerCheckBox.setSelected(bookmark.getLayerId() == null);
        noLayerCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel();
            }
        });

        dataLayerComboBox.setModel(new DefaultComboBoxModel(heatmap.getLayers().getLayerNames().toArray()));
        dataLayerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel();
            }
        });

        // ROWS AND COLUMNS

        noColumnsCheckBox.setSelected(bookmark.getColumns() == null);
        noColumnsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel();
            }
        });

        noRowsCheckBox.setSelected(bookmark.getRows() == null);
        noRowsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel();
            }
        });


        // DELETE button
        if (creating) {
            setLogo(IconUtils.getImageIconResource(IconNames.bookmarkAdd48));
            setTitle("Create Bookmark");
            deleteThisBookmarkButton.setEnabled(false);
            deleteThisBookmarkButton.setVisible(false);
        } else {
            setLogo(IconUtils.getImageIconResource(IconNames.bookmark48));
            setTitle("Edit Bookmark");
            forbiddenNames.remove(bookmark.getName());

            deleteThisBookmarkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delete = true;
                    nameField.setEnabled(false);
                    noLayerCheckBox.setEnabled(false);
                    dataLayerComboBox.setEnabled(false);
                    restoreBackup();
                    setMessage(MessageStatus.PROGRESS, "Click OK to remove the Bookmark");
                    setComplete(true);
                }
            });

        }

        setMessage(MessageStatus.INFO, "Choose a name for the Bookmark");

        //BOOKMARK NAME
        nameField.setText(bookmark.getName());
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateModel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateModel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateModel();
            }
        });

        updateControls();
        nameField.selectAll();
    }

    private void restoreBackup() {
        this.bookmark = backup;
    }


    public boolean isDelete() {
        return delete;
    }


    @Override
    public void updateModel() {
        bookmark.setName(nameField.getText());

        if (noColumnsCheckBox.isSelected()) {
            bookmark.setColumns(null);
        } else {
            bookmark.setColumns(backup.getColumns());
        }

        if (noRowsCheckBox.isSelected()) {
            bookmark.setRows(null);
        } else {
            bookmark.setRows(backup.getRows());
        }

        if (noLayerCheckBox.isSelected()) {
            bookmark.setLayerId(null);
        } else {
            bookmark.setLayerId(backup.getLayerId());
        }

        updateControls();
    }

    @Override
    public void updateControls() {

        String rows = bookmark.getRows() == null ? "-" : String.valueOf(bookmark.getRows().size());
        String cols = bookmark.getColumns() == null ? "-" : String.valueOf(bookmark.getColumns().size());
        rowNbLabel.setText("<html><b>" + rows + "</b></html>");
        colNbLabel.setText("<html><b>" + cols + "</b></html>");

        dataLayerComboBox.setSelectedItem(bookmark.getLayerId());
        if (noLayerCheckBox.isSelected()) {
            dataLayerComboBox.setEnabled(false);
        } else {
            dataLayerComboBox.setEnabled(true);
        }

        if (bookmark.getName().equals("new Bookmark") || bookmark.getName().equals("")) {
            setComplete(false);
            return;
        }
        if (!uniqueName()) {
            if (creating) {
                setMessage(MessageStatus.WARN, "There is already a bookmark with this name and will be overwritten");
            } else {
                setMessage(MessageStatus.ERROR, "There is already a bookmark with this name");
                setComplete(false);
                return;
            }
        } else {
            setMessage(MessageStatus.INFO, "Choose a name for the Bookmark");
        }

        setComplete(true);
    }

    private boolean uniqueName() {
/*        for (Bookmark b : existingBookmarks.getAll()) {
            if (oldName != null && !oldName.equals(bookmark.getName()) && b.getName().equals(bookmark.getName())) {
                return false;
            }
        }*/
        return (!forbiddenNames.contains(bookmark.getName()));
    }

    @Override
    public JComponent createControls() {
        return panel;
    }


}

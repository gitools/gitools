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

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookmarkEditPage extends AbstractWizardPage {
    private final Bookmarks existingBookmarks;
    private JPanel panel;
    private JTextField nameField;
    private JLabel rowNbLabel;
    private JLabel colNbLabel;
    private JComboBox dataLayerComboBox;
    private JCheckBox noneCheckBox;
    private JButton deleteThisBookmarkButton;
    private boolean delete = false;
    private boolean creating;

    public Bookmark getBookmark() {
        return bookmark;
    }

    private Bookmark bookmark;

    public BookmarkEditPage(Heatmap heatmap, final Bookmark bookmark, boolean creating) {
        this.bookmark = bookmark;
        this.existingBookmarks = heatmap.getBookmarks();
        this.creating = creating;

        // DATA LAYER

        noneCheckBox.setSelected(bookmark.getLayerId() == null);
        noneCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!noneCheckBox.isSelected()) {
                    bookmark.setLayerId((String) dataLayerComboBox.getSelectedItem());
                } else {
                    bookmark.setLayerId(null);
                }
                updateControls();
            }
        });

        dataLayerComboBox.setModel(new DefaultComboBoxModel(heatmap.getLayers().getLayerNames().toArray()));
        dataLayerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookmark.setLayerId((String) dataLayerComboBox.getSelectedItem());
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

            deleteThisBookmarkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delete = true;
                    nameField.setEnabled(false);
                    noneCheckBox.setEnabled(false);
                    dataLayerComboBox.setEnabled(false);
                    setMessage(MessageStatus.PROGRESS, "Click OK to remove the Bookmark");
                    setComplete(true);
                }
            });

        }

        setMessage(MessageStatus.INFO, "Choose a name for the Bookmark");
        rowNbLabel.setText("<html><b>" + bookmark.getRows().size() + "</b></html>");
        colNbLabel.setText("<html><b>" + bookmark.getColumns().size() + "</b></html>");

        //BOOKMARK NAME
        nameField.setText(bookmark.getName());
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateName();
            }
        });

        updateControls();
        nameField.selectAll();
    }


    public boolean isDelete() {
        return delete;
    }


    private void updateName() {
        bookmark.setName(nameField.getText());
        updateControls();
    }

    @Override
    public void updateControls() {

        if (noneCheckBox.isSelected()) {
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
        for (Bookmark b : existingBookmarks.getAll()) {
            if (!b.equals(bookmark) && b.getName().equals(bookmark.getName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public JComponent createControls() {
        return panel;
    }


}

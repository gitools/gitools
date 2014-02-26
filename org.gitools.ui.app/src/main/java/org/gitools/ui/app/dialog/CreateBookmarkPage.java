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
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;

public class CreateBookmarkPage extends AbstractWizardPage {
    private final List<Bookmark> existingBookmarks;
    private JPanel panel;
    private JTextField nameField;
    private JTextPane descriptionPane;

    public Bookmark getBookmark() {
        return bookmark;
    }

    private Bookmark bookmark;

    public CreateBookmarkPage(List<Bookmark> bookmarks, Bookmark bookmark) {
        this.bookmark = bookmark;
        this.existingBookmarks = bookmarks;
        nameField.setText(bookmark.getName());
        setTitle("Create Bookmark");
        setMessage(MessageStatus.INFO, "Choose a name for the new Bookmark");
        descriptionPane.setText("<html><body>Creating bookmark consisting of " +
                "<b>" + bookmark.getRows().size() + "</b> rows and " +
                "<b>" + bookmark.getColumns().size() + "</b> columns.</body></html>");

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
    }

    private void updateName() {
        bookmark.setName(nameField.getText());
        updateControls();
    }

    @Override
    public void updateControls() {
        if (bookmark.getName().equals("new Bookmark") || bookmark.getName().equals("")) {
            setComplete(false);
            return;
        }
        if (!uniqueName()) {
            setMessage(MessageStatus.WARN, "There is already a bookmark with name and will be overwritten");
        } else {
            setMessage(MessageStatus.INFO, "Choose a name for the new Bookmark");
        }

        setComplete(true);
    }

    private boolean uniqueName() {
        for (Bookmark b : existingBookmarks) {
            if (b.getName().equals(bookmark.getName())) {
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

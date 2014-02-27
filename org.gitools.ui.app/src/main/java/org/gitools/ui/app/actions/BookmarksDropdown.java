package org.gitools.ui.app.actions;

import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Bookmarks;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.platform.actions.BaseAction;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class BookmarksDropdown extends BaseAction {
    private JComboBox<Bookmark> bookmarkComboBox;
    private JPanel bookmarksSelectPanel;
    private ListComboBoxModel<Bookmark> model;

    Bookmarks bookmarks;


    public BookmarksDropdown() {
        super("Select bookmark");
        bookmarks = null;
    }


    public void setBookmarks(Bookmarks bookmarks) {
        this.bookmarks = bookmarks;
        this.model = new ListComboBoxModel<Bookmark>(bookmarks.getAll());
        bookmarkComboBox.setModel(model);
    }

    public JPanel getPanel() {
        return bookmarksSelectPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void setEnabled(boolean newValue) {
        super.setEnabled(newValue);
        bookmarksSelectPanel.setVisible(newValue && bookmarks.getAll().size() > 0);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap;
    }
}

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
package org.gitools.ui.app.actions;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Bookmarks;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.IPanelAction;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


//FIXME: always two listeners.
//TODO: remove bookmark!

public class BookmarksDropdown extends HeatmapAction implements IPanelAction, PropertyChangeListener {

    private JComboBox<Bookmark> bookmarkComboBox;
    private JPanel bookmarksSelectPanel;
    private Bookmarks bookmarks;
    private static Bookmark NO_OPTION = new Bookmark();

    public BookmarksDropdown() {
        super("Bookmarks dropdown");

        bookmarksSelectPanel.setVisible(false);

        bookmarkComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bookmark b = (Bookmark) bookmarkComboBox.getSelectedItem();
                if (b != null && !b.equals(NO_OPTION)) {
                    getHeatmap().applyBookmark(b);
                    Application.get().setStatusText("Bookmark " + b.getName() + " applied.");
                    bookmarkComboBox.getModel().setSelectedItem(NO_OPTION);
                }
            }
        });
    }

    @Override
    public boolean isEnabledByModel(Object model) {

        if (model instanceof Heatmap) {
            Bookmarks b = ((Heatmap) model).getBookmarks();
            b.addPropertyChangeListener(Bookmarks.PROPERTY_CONTENTS, this);
            setBookmarks(b);
            bookmarksSelectPanel.repaint();
            return true;

        }
        bookmarksSelectPanel.setVisible(false);
        bookmarksSelectPanel.repaint();
        return false;
    }

    private void setBookmarks(final Bookmarks bookmarks) {
        this.bookmarks = bookmarks;
        Bookmark[] bookmarkArray = bookmarks.getAll().toArray(new Bookmark[bookmarks.getAll().size()]);
        bookmarksSelectPanel.setVisible(bookmarks.getAll().size() > 0);
        if (bookmarks.getAll().size() == 0) return;

        NO_OPTION.setName(String.valueOf(bookmarkArray.length) + (bookmarkArray.length > 1 ? " Bookmarks:" : " Bookmark:"));
        bookmarkArray = (Bookmark[]) ArrayUtils.add(bookmarkArray, 0, NO_OPTION);
        bookmarkComboBox.setModel(new DefaultComboBoxModel<>(bookmarkArray));
        bookmarkComboBox.getModel().setSelectedItem(NO_OPTION);

    }

    @Override
    public JPanel getPanel() {
        bookmarksSelectPanel.setOpaque(false);
        return bookmarksSelectPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Nothing
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!bookmarks.equals(evt.getNewValue())) {
            bookmarks.removePropertyChangeListener(this);
        }
        setBookmarks(getHeatmap().getBookmarks());
        bookmarkComboBox.contentsChanged(new ListDataEvent(evt, 0, 0, 0));
        bookmarksSelectPanel.revalidate();
        bookmarksSelectPanel.repaint();
    }
}

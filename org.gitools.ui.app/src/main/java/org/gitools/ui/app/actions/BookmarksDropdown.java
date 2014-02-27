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

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueModel;
import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Bookmarks;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.actions.IPanelAction;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class BookmarksDropdown extends HeatmapAction implements IPanelAction, PropertyChangeListener {

    private JComboBox<Bookmark> bookmarkComboBox;
    private JPanel bookmarksSelectPanel;

    public BookmarksDropdown() {
        super("Bookmarks dropdown");

        bookmarkComboBox.setVisible(false);
    }

    @Override
    public boolean isEnabledByModel(Object model) {

        if (model instanceof Heatmap) {
            setBookmarks(((Heatmap) model).getBookmarks());
            bookmarkComboBox.setVisible(true);
            bookmarksSelectPanel.repaint();
            return true;

        }
        bookmarkComboBox.setVisible(false);
        bookmarksSelectPanel.repaint();
        return false;
    }

    private static String NO_SELECTION = "-- none --";

    private void setBookmarks(final Bookmarks bookmarks) {

        ListModel allBookmarksModel = new AbstractListModel() {
            @Override
            public int getSize() {
                return bookmarks.getAll().size() + 1;
            }

            @Override
            public Object getElementAt(int index) {

                if (index == 0) {
                    return NO_SELECTION;
                }

                return bookmarks.getAll().get(index - 1).getName();
            }
        };

        ValueModel selectedBookmark = new AbstractValueModel() {
            @Override
            public Object getValue() {
                return bookmarks.getSelected();
            }

            @Override
            public void setValue(Object newValue) {
                if (newValue == NO_SELECTION) {
                    bookmarks.setSelected(null);
                } else {
                    bookmarks.setSelected((String) newValue);
                }
            }
        };

        bookmarks.addPropertyChangeListener(Bookmarks.PROPERTY_SELECTED, this);

        Bindings.bind(bookmarkComboBox, new SelectionInList<>(allBookmarksModel, selectedBookmark));
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
        bookmarksSelectPanel.revalidate();
        bookmarksSelectPanel.repaint();
    }
}

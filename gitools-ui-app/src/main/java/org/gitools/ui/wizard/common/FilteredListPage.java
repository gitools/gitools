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
package org.gitools.ui.wizard.common;

import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @noinspection ALL
 */
public class FilteredListPage extends AbstractWizardPage {

    private FilteredListPanel panel;

    protected FilteredListPage() {
    }

    public FilteredListPage(@NotNull Object[] listData) {
        panel.setListData(listData);
    }

    public FilteredListPage(@NotNull Object[] listData, int selectionMode) {
        panel.setListData(listData);
        panel.setSelectionMode(selectionMode);
    }

    @Override
    public JComponent createControls() {
        panel = new FilteredListPanel() {
            @Override
            protected void selectionChanged() {
                FilteredListPage.this.selectionChanged();
            }
        };

        return panel;
    }

    private void selectionChanged() {
        Object value = panel.getSelectedValue();
        setComplete(value != null);
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public void setSelectionMode(int mode) {
        panel.setSelectionMode(mode);
    }

    public String getFilterText() {
        return panel.getFilterText();
    }

    protected void setListData(@NotNull Object[] listData) {
        panel.setListData(listData);
    }

    protected Object getSelectedValue() {
        return panel.getSelectedValue();
    }

    protected void setSelectedValue(Object o) {
        panel.setSElectedValue(o);
    }
}

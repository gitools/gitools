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
package org.gitools.ui.app.datasources.biomart.wizard;

import org.gitools.datasources.biomart.BiomartService;
import org.gitools.datasources.biomart.restful.model.MartLocation;
import org.gitools.ui.core.pages.common.FilteredListPanel;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class BiomartDatabasePage extends AbstractWizardPage {


    private static class DatabaseListWrapper {
        private final MartLocation mart;

        public DatabaseListWrapper(MartLocation mart) {
            this.mart = mart;
        }

        public MartLocation getMart() {
            return mart;
        }

        @Override
        public String toString() {
            return mart.getDisplayName();
        }
    }

    private final BiomartService biomartService;

    private FilteredListPanel panelDataset;

    private boolean updated;

    public BiomartDatabasePage(BiomartService biomartService /*IBiomartService biomartService*/) {
        super();

        this.biomartService = biomartService;
        updated = false;

        setTitle("Select database");


    }

    @Override
    public JComponent createControls() {
        panelDataset = new FilteredListPanel();

        panelDataset.list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectionChangeActionPerformed();
            }
        });

        return panelDataset;
    }

    @Override
    public void updateControls() {
        if (updated) {
            return;
        }

        setMessage(MessageStatus.PROGRESS, "Retrieving databases...");

        panelDataset.setListData(new Object[]{});

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<MartLocation> registry = biomartService.getRegistry();
                    final List<DatabaseListWrapper> listData = new ArrayList<DatabaseListWrapper>();
                    for (MartLocation mart : registry)
                        if (mart.getVisible() != 0) {
                            listData.add(new DatabaseListWrapper(mart));
                        }

                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            panelDataset.setListData(listData.toArray(new DatabaseListWrapper[listData.size()]));

                            setMessage(MessageStatus.INFO, "");
                        }
                    });

                    updated = true;
                } catch (Exception e) {
                    setStatus(MessageStatus.ERROR);
                    setMessage(e.getMessage());
                    ExceptionDialog dlg = new ExceptionDialog(Application.get(), e);
                    dlg.setVisible(true);
                }
            }
        }).start();
    }

    public MartLocation getMart() {
        DatabaseListWrapper model = (DatabaseListWrapper) panelDataset.getSelectedValue();
        return model.getMart();
    }

    private void selectionChangeActionPerformed() {
        Object value = panelDataset.list.getSelectedValue();
        setComplete(value != null);
    }
}

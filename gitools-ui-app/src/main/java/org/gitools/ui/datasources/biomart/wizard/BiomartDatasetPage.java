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
package org.gitools.ui.datasources.biomart.wizard;

import org.gitools.datasources.biomart.BiomartService;
import org.gitools.datasources.biomart.restful.model.DatasetInfo;
import org.gitools.datasources.biomart.restful.model.MartLocation;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.wizard.common.FilteredListPanel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class BiomartDatasetPage extends AbstractWizardPage {

    private static class DatasetListWrapper {

        private final DatasetInfo dataset;

        public DatasetListWrapper(DatasetInfo dataset) {
            this.dataset = dataset;
        }

        public DatasetInfo getDataset() {
            return dataset;
        }

        @Override
        public String toString() {
            return dataset.getDisplayName();
        }
    }

    private final BiomartService biomartService;

    @Nullable
    private MartLocation mart;

    private FilteredListPanel panelDataset;

    private boolean updated;

    /**
     * @noinspection UnusedDeclaration
     */
    public BiomartDatasetPage(BiomartService biomartService /*IBiomartService biomartService*/) {
        super();

        this.biomartService = biomartService;

        this.mart = null;
        updated = false;

        setTitle("Select dataset");
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

        if (mart == null) {
            return;
        }

        if (updated) {
            return;
        }

        setStatus(MessageStatus.PROGRESS);
        setMessage("Retrieving datasets for " + mart.getDisplayName() + " ...");

        panelDataset.setListData(new Object[]{});

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    List<DatasetInfo> dataSets = biomartService.getDatasets(mart);
                    final List<DatasetListWrapper> visibleDataSets = new ArrayList<DatasetListWrapper>();
                    for (DatasetInfo ds : dataSets) {
                        if (ds.getVisible() != 0) {
                            visibleDataSets.add(new DatasetListWrapper(ds));
                        }
                    }

                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            panelDataset.setListData(visibleDataSets.toArray(new DatasetListWrapper[visibleDataSets.size()]));

                            setMessage(MessageStatus.INFO, "");
                        }
                    });

                    updated = true;
                } catch (Exception e) {
                    setStatus(MessageStatus.ERROR);
                    setMessage(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setMart(MartLocation mart) {
        if (this.mart != mart) {
            updated = false;
        }

        this.mart = mart;
    }

    public DatasetInfo getDataset() {
        DatasetListWrapper wrapper = (DatasetListWrapper) panelDataset.getSelectedValue();
        return wrapper.getDataset();
    }

    private void selectionChangeActionPerformed() {
        Object value = panelDataset.list.getSelectedValue();
        setComplete(value != null);
    }
}

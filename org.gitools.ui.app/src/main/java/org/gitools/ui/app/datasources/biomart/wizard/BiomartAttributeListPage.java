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
import org.gitools.datasources.biomart.restful.model.*;
import org.gitools.ui.app.datasources.biomart.panel.BiomartAttributeListPanel;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.dialog.ExceptionGlassPane;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.util.List;

/**
 * @noinspection ALL
 */
public class BiomartAttributeListPage extends AbstractWizardPage {

    private MartLocation mart;

    private DatasetInfo dataset;

    private List<AttributePage> attrPages;

    private BiomartAttributeListPanel panel;

    private BiomartService biomartService;

    private DatasetConfig biomartConfig;

    private Boolean reloadData; //determine whether update panel data

    public BiomartAttributeListPage() {
    }

    @Override
    public JComponent createControls() {
        panel = new BiomartAttributeListPanel();
        panel.addAttributeListChangeListener(new BiomartAttributeListPanel.AttributeListChangeListener() {
            @Override
            public void listChanged() {
                setComplete(panel.getAttributeListSize() > 0);
            }
        });

        return panel;
    }

    @Override
    public void updateControls() {
        //panel.setAddBtnEnabled(false);

        if (panel != null && reloadData) {
            panel.removeAllListAttributes();
        }

        panel.setAttributePages(null);

        if (panel.getAttributePages() == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    updateControlsThread();
                    reloadData = false; //To avoid reload data when back button

                }
            }).start();
        }


    }

    private void updateControlsThread() {
        try {
            if (attrPages == null || reloadData) {
                setStatus(MessageStatus.PROGRESS);
                setMessage("Retrieving available attributes ...");
                biomartConfig = biomartService.getConfiguration(dataset);
                attrPages = biomartConfig.getAttributePages();
            }

            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    //panel.setAddBtnEnabled(true);
                    panel.setAttributePages(attrPages);

                    setStatus(MessageStatus.WARN);
                    setMessage("Press [Add...] button to select attributes");
                }
            });
        } catch (final Throwable cause) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setStatus(MessageStatus.ERROR);
                    setMessage(cause.getClass().getSimpleName() + ": " + cause.getMessage());
                    ExceptionGlassPane dlg = new ExceptionGlassPane(Application.get(), cause);
                    dlg.setVisible(true);
                }
            });
        }
    }

	/*private void setAttributePages(List<AttributePage> attrPages) {
        panel.setAttributePages(attrPages);
	}*/

    public void setSource(BiomartService biomartService, MartLocation mart, DatasetInfo ds) {

        if (this.dataset != null && this.dataset.getName().equals(ds.getName())) {
            reloadData = false;
        } else {
            reloadData = true;
        }

        this.biomartService = biomartService;
        this.mart = mart;
        this.dataset = ds;
    }

    public List<AttributeDescription> getAttributeList() {
        return panel.getAttributeList();
    }

    public DatasetConfig getBiomartConfig() {
        return biomartConfig;
    }
}

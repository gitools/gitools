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
import org.gitools.datasources.biomart.restful.model.*;
import org.gitools.ui.datasources.biomart.panel.AttributesTreeModel;
import org.gitools.ui.datasources.biomart.panel.AttributesTreeModel.AttributeWrapper;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.wizard.common.FilteredTreePage;
import org.gitools.ui.wizard.common.FilteredTreePanel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class BiomartAttributePage extends FilteredTreePage {


    private MartLocation mart;


    private DatasetInfo dataset;


    private List<AttributePage> attrPages;

    private AttributeDescription attribute;

    private BiomartService biomartService;

    private DatasetConfig biomartConfig;

    private Boolean reloadData; //determine whether update panel data

    public BiomartAttributePage() {
        super();

        this.mart = null;
        this.dataset = null;
        this.attrPages = null;

    }

    @Override
    public JComponent createControls() {
        JComponent component = super.createControls();
        panel.tree.setRootVisible(false);
        panel.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        panel.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                selectionChanged();
            }
        });

        return component;
    }

    @Override
    public void updateControls() {

        setComplete(false);

        panel.setModel(new AttributesTreeModel(new ArrayList<AttributePage>(0)));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (attrPages == null || reloadData) {
                        setStatus(MessageStatus.PROGRESS);
                        setMessage("Retrieving available attributes ...");

                        biomartConfig = biomartService.getConfiguration(dataset);
                        attrPages = biomartConfig.getAttributePages();

                    }

                    final AttributesTreeModel model = new AttributesTreeModel(attrPages);

                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            FilteredTreePanel p = getPanel();
                            p.setModel(model);
                            p.expandAll();
                            setMessage(MessageStatus.INFO, "");
                        }
                    });

                } catch (final Throwable cause) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setStatus(MessageStatus.ERROR);
                            setMessage(cause.getClass().getSimpleName() + ": " + cause.getMessage());
                            ExceptionDialog dlg = new ExceptionDialog(AppFrame.get(), cause);
                            dlg.setVisible(true);
                        }
                    });
                }
            }
        }).start();
    }

    private void selectionChanged() {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) panel.tree.getLastSelectedPathComponent();

        boolean complete = false;

        if (node != null && (node.getUserObject() instanceof AttributeWrapper)) {
            AttributeWrapper aw = (AttributeWrapper) node.getUserObject();
            complete = aw.getType() == AttributeWrapper.NodeType.ATTRIBUTE;
            if (complete) {
                attribute = (AttributeDescription) aw.getObject();
            }
        }

        setComplete(complete);
    }

    public void setSource(BiomartService biomartService, MartLocation mart, DatasetInfo dataset) {

        if (this.dataset != null && this.dataset.getName().equals(dataset.getName())) {
            reloadData = false;
        } else {
            reloadData = true;
        }

        this.biomartService = biomartService;
        this.mart = mart;
        this.dataset = dataset;
    }

    public synchronized void setAttributePages(List<AttributePage> attrPages) {
        this.attrPages = attrPages;
        reloadData = false;
    }


    public synchronized List<AttributePage> getAttributePages() {
        return attrPages;
    }

    public AttributeDescription getAttribute() {
        return attribute;
    }


    @Override
    protected TreeModel createModel(String filterText) {
        return new AttributesTreeModel(attrPages, filterText);
    }

    public DatasetConfig getBiomartConfig() {
        return biomartConfig;
    }
}

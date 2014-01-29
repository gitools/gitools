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
import org.gitools.datasources.biomart.restful.model.DatasetConfig;
import org.gitools.datasources.biomart.restful.model.DatasetInfo;
import org.gitools.datasources.biomart.restful.model.Filter;
import org.gitools.datasources.biomart.restful.model.FilterCollection;
import org.gitools.datasources.biomart.restful.model.FilterGroup;
import org.gitools.datasources.biomart.restful.model.FilterPage;
import org.gitools.datasources.biomart.restful.model.Option;
import org.gitools.ui.app.datasources.biomart.filter.FilterCollectionPanel;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @noinspection ALL
 */
public class BiomartFilterConfigurationPage extends AbstractWizardPage {


    // Biomart Configuration Wrappers
    private static class PageListWrapper {

        private final FilterPage page;

        public PageListWrapper(FilterPage dataset) {
            this.page = dataset;
        }

        public FilterPage getFilterPage() {
            return page;
        }

        @Override
        public String toString() {

            String res = page.getDisplayName();
            if (res != null) {
                res = res.replace(":", "");
            }

            return res;
        }
    }

    private static class GroupListWrapper {

        private final FilterGroup group;

        public GroupListWrapper(FilterGroup dataset) {
            this.group = dataset;
        }

        public FilterGroup getFilterGroup() {
            return group;
        }

        @Override
        public String toString() {

            String res = group.getDisplayName();
            if (res != null) {
                res = res.replace(":", "");
            }

            return res;
        }
    }

    private final Integer FILTER_PANEL_WEIGHT = 333;

    private final Integer FILTER_PANEL_HEIGHT = 330;

    private DatasetInfo dataset;

    private DatasetConfig biomartConfig;

    private BiomartService biomartService;


    private FilterGroup lastGroupSelected;


    private FilterPage lastPageSelected;

    private final HashMap<String, Filter> filters;

    private HashMap<FilterPage, CollectionsPanelsCache> collectionsCache; // Stores component panel selections

    private Boolean reloadData; //determine whether update panel data

    private HashMap<String, List<Option>> defaultSelecComposData; //Stores default selection component data

    public static class CollectionsPanelsCache {


        public final HashMap<FilterGroup, List<FilterCollectionPanel>> collections = new HashMap<FilterGroup, List<FilterCollectionPanel>>();

    }

    /*
     * Default class constructor.
     * Member attributes and graphic components are initialised
     *
     */
    public BiomartFilterConfigurationPage() {

        initComponents();

        lastGroupSelected = null;

        lastPageSelected = null;

        reloadData = true;

        filters = new HashMap<String, Filter>();

        collectionsCache = new HashMap<FilterPage, CollectionsPanelsCache>();

        setComplete(true); //Next button always is true, input filters is not mandatory

        filterPageCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (filterPageCombo.getSelectedItem() != null) {

                        updateGroupFilterList(((PageListWrapper) filterPageCombo.getSelectedItem()).getFilterPage());

                        lastPageSelected = ((PageListWrapper) filterPageCombo.getSelectedItem()).getFilterPage();
                    }
                }

            }
        });

        filterGroupList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (filterPageCombo.getModel().getSelectedItem() != null && filterGroupList.getSelectedValue() != null) {
                    updateCollectionControls(((PageListWrapper) filterPageCombo.getModel().getSelectedItem()).getFilterPage(), ((GroupListWrapper) filterGroupList.getSelectedValue()).getFilterGroup());

                    lastGroupSelected = ((GroupListWrapper) filterGroupList.getSelectedValue()).getFilterGroup();
                }
            }
        });

    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterPageCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        filterGroupList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        scrollPanel = new javax.swing.JScrollPane();
        collectionsPanel = new javax.swing.JPanel();

        setBorder(null);

        jLabel1.setText("Page");

        jSplitPane1.setDividerLocation(198);
        jSplitPane1.setContinuousLayout(true);

        filterGroupList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(filterGroupList, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(filterGroupList, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE).addContainerGap()));

        jSplitPane1.setLeftComponent(jPanel1);

        scrollPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPanel.setViewportBorder(null);

        collectionsPanel.setBorder(null);

        javax.swing.GroupLayout collectionsPanelLayout = new javax.swing.GroupLayout(collectionsPanel);
        collectionsPanel.setLayout(collectionsPanelLayout);
        collectionsPanelLayout.setHorizontalGroup(collectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 350, Short.MAX_VALUE));
        collectionsPanelLayout.setVerticalGroup(collectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 366, Short.MAX_VALUE));

        scrollPanel.setViewportView(collectionsPanel);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 362, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 390, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE).addContainerGap())));

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(filterPageCombo, 0, 522, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(filterPageCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE).addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel collectionsPanel;
    private javax.swing.JList filterGroupList;
    private javax.swing.JComboBox filterPageCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JScrollPane scrollPanel;
    // End of variables declaration//GEN-END:variables


    @Override
    public JComponent createControls() {
        return this;
    }

    /**
     * Method called each time this class is shown in the application
     * All components from collections panel are cleaned, to avoid wrong
     * component visualisations
     */
    @Override
    public void updateControls() {

        //Clean main collections panel
        collectionsPanel.removeAll();

        collectionsPanel.setPreferredSize(new Dimension(FILTER_PANEL_WEIGHT, FILTER_PANEL_HEIGHT));

        filterGroupList.clearSelection();

        lastGroupSelected = null;

        lastPageSelected = null;

        if (reloadData) {

            setMessage(MessageStatus.PROGRESS, "Retrieving available filters ...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        defaultSelecComposData = new HashMap<String, List<Option>>();

                        initCollectionsCache();
                        updatePageFilterList();

                        if (filterPageCombo.getSelectedItem() != null) {
                            lastPageSelected = ((PageListWrapper) filterPageCombo.getSelectedItem()).getFilterPage();
                            setMessage(MessageStatus.INFO, "");

                        } else {
                            lastPageSelected = null;
                            setMessage(MessageStatus.INFO, " No filters are availables");
                        }

                        reloadData = false;

                    } catch (final Exception ex) {

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {

                                setStatus(MessageStatus.ERROR);
                                setMessage(ex.getMessage());
                            }
                        });

                        ExceptionDialog dlg = new ExceptionDialog(Application.get(), ex);
                        dlg.setVisible(true);
                        System.out.println(ex);
                    }
                }

            }).start();
        }
    }

    void updatePageFilterList() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        if (biomartConfig.getFilterPages() != null) {
            for (FilterPage p : biomartConfig.getFilterPages()) {
                if (!p.isHidden() && !p.isHideDisplay()) {
                    model.addElement(new PageListWrapper(p));
                    updateGroupFilterList(p);
                }
            }
        }

        this.filterPageCombo.setModel(model);
    }

    private void updateGroupFilterList(FilterPage page) {

        // Avoid update process when null value or unaltered filter group selection
        if (lastPageSelected != null && lastPageSelected.getInternalName().equals(page.getInternalName())) {

            return;
        }

        //Clean main collections panel
        collectionsPanel.removeAll();

        collectionsPanel.setPreferredSize(new Dimension(FILTER_PANEL_WEIGHT, FILTER_PANEL_HEIGHT));

        lastGroupSelected = null;

        DefaultListModel model = new DefaultListModel();

        for (FilterGroup group : page.getFilterGroups()) {
            if (!group.isHidden() && !group.isHideDisplay()) {

                model.addElement(new GroupListWrapper(group));

                updateCollectionsCache(page, group);
            }
        }

        this.filterGroupList.setModel(model);
    }

    /**
     * Stores in a field (memory) all Panels (collectionsPanels, descriptionPanels, components,...)
     * but do not show them on the screen
     *
     * @param page
     * @param group
     */
    private void updateCollectionsCache(FilterPage page, FilterGroup group) {

        FilterCollectionPanel collectionPanel;

        List<FilterCollectionPanel> listCollections = new ArrayList<FilterCollectionPanel>();

        for (FilterCollection collection : group.getFilterCollections()) {
            if (!collection.isHidden() && !collection.isHideDisplay()) {
                collectionPanel = new FilterCollectionPanel(collection, this);
                if (collectionPanel.isPanelRendered()) {
                    listCollections.add(collectionPanel); //add collectionPanel in list
                }
            }
        }

        //add collectionPanel list in cache
        collectionsCache.get(page).collections.put(group, listCollections);
    }

    /**
     * Shows in the screen all collections panels which belongs to the group selected
     *
     * @param page
     * @param group
     */
    private void updateCollectionControls(FilterPage page, FilterGroup group) {

        // Avoid update process when null value or unaltered filter group selection
        if (filterGroupList.getSelectedValue() == null || (lastGroupSelected != null && lastGroupSelected.getInternalName().equals(group.getInternalName()))) {

            return;
        }

        Integer collectionPanelHeight = 0;

        collectionsPanel.removeAll();

        collectionsPanel.repaint();

        collectionsPanel.setLayout(new BoxLayout(collectionsPanel, BoxLayout.Y_AXIS));

        //Check if collections have not been load previously (cache)
        if (collectionsCache.get(page).collections.get(group).size() == 0) {
            updateCollectionsCache(page, group);
        }

        for (FilterCollectionPanel col : collectionsCache.get(page).collections.get(group)) {

            collectionsPanel.add(col);

            collectionPanelHeight += col.getCurrentHeigh();
        }

        Dimension d = new Dimension(collectionsPanel.getWidth(), collectionPanelHeight);

        collectionsPanel.setPreferredSize(d);
        collectionsPanel.repaint();
        scrollPanel.validate();

        validate();
    }

    /**
     * Loop through all groups and their collections.
     * If checkBox collection checked the filter is annotated
     *
     * @return
     */

    public Collection<Filter> getFilters() {

        List<Filter> listFilters = new ArrayList<Filter>();

        for (FilterPage page : collectionsCache.keySet())

            for (FilterGroup group : collectionsCache.get(page).collections.keySet())

                for (FilterCollectionPanel panel : collectionsCache.get(page).collections.get(group))

                    listFilters.addAll(panel.getFilters());

        return listFilters;
    }

    /**
     * Load global attributes and guess if reload data
     *
     * @param service
     * @param dataset
     */
    public void setSource(BiomartService service, DatasetConfig config) {

        if (this.biomartConfig != null && this.biomartConfig.getDataset().equals(config.getDataset())) {
            reloadData = false;
        } else {
            reloadData = true;
            filterPageCombo.setModel(new DefaultComboBoxModel());
            filterGroupList.setModel(new DefaultListModel());
            collectionsPanel.removeAll();
            collectionsPanel.repaint();
            scrollPanel.validate();

            validate();
        }

        this.biomartService = service;
        this.biomartConfig = config;
    }

    /**
     * Initialisation of the field which will contain all filter panels
     */
    private void initCollectionsCache() {

        if (collectionsCache == null) {
            collectionsCache = new HashMap<FilterPage, CollectionsPanelsCache>();
        } else {
            collectionsCache.clear();
        }

        for (FilterPage page : biomartConfig.getFilterPages()) {

            if (!page.isHidden() && !page.isHideDisplay()) {

                collectionsCache.put(page, null);

                CollectionsPanelsCache panels = new CollectionsPanelsCache();

                for (FilterGroup group : page.getFilterGroups())

                    panels.collections.put(group, new ArrayList(0));

                collectionsCache.put(page, panels);
            }
        }

    }

    public void setFilter(String name, Filter f) {
        filters.put(name, f);
    }

    public void setFilters(HashMap<String, Filter> filters) {
        filters.putAll(filters);
    }

    public void deleteFilter(String name) {
        filters.remove(name);
    }

    public void deleteFilters(HashMap<String, Filter> delFilters) {

        for (String name : delFilters.keySet())
            filters.remove(name);
    }

    public BiomartService getBiomartService() {
        return this.biomartService;
    }

    public DatasetConfig getDatasetConfig() {
        return this.biomartConfig;
    }

    public HashMap<FilterPage, CollectionsPanelsCache> getCollectionsCache() {
        return collectionsCache;
    }

    public void setCollectionsCache(HashMap<FilterPage, CollectionsPanelsCache> collectionsCache) {
        this.collectionsCache = collectionsCache;
    }

    public HashMap<String, List<Option>> getDefaultSelecComposData() {
        return defaultSelecComposData;
    }


    public void storeSelecComponentsDefaultData(HashMap<String, List<Option>> data) {

        if (data.size() > 0) {
            for (String key : data.keySet())
                this.defaultSelecComposData.put(key, data.get(key));
        }
    }

	/*
    public void resetCollectionCache() {

		if (collectionsCache == null) return;

		for (FilterPage p : collectionsCache.keySet())
		{
			for(FilterGroup g : collectionsCache.get(p).collections.keySet())
			{
				for ( FilterCollectionPanel f : collectionsCache.get(p).collections.get(g))
					this.remove(f);
			}
			collectionsCache.get(p).collections.clear();
		}
		collectionsCache.clear();
		this.validate();

	}
	*/
}

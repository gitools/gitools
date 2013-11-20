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
package org.gitools.ui.datasources.biomart.filter;

import org.gitools.datasources.biomart.restful.model.*;

import javax.swing.*;
import java.util.List;

/**
 * @noinspection ALL
 */
public class FilterDescriptionPanel extends javax.swing.JPanel {

    private final FilterCollectionPanel parentCollection;
    private FilterDescription filterDescription;
    private FilterComponent filterComponent;
    private Integer currentHeight;
    private Boolean renderPanel;
    private Boolean renderLabel;

    public FilterDescriptionPanel(FilterDescription description, FilterCollectionPanel collectionParent, boolean labelRendered) {

        this.filterDescription = description;
        this.parentCollection = collectionParent;
        this.currentHeight = 0;
        this.renderLabel = labelRendered;

        if (filterDescription.getDisplayType() != null) {

            this.renderPanel = true;

            buildComponent();

        } else if (filterDescription.getPointerDataset() != null) {

            try {
                DatasetInfo d = new DatasetInfo();

                d.setName(filterDescription.getPointerDataset());

                d.setInterface(filterDescription.getPointerInterface());

                DatasetConfig configuration = getParentCollection().getFilterConfigurationPage().getBiomartService().getConfiguration(d);

                buildPointerFilterComponents(configuration);

                renderPanel = true;
            } catch (final Exception ex) {

                System.out.println("Pointer dataset :" + filterDescription.getPointerDataset() + " has not been found");

                renderPanel = false;

                return;
            }
        } else {
            this.renderPanel = false;
        }
    }

    /**
     * BuildPointer elements with default options if it is the case
     *
     * @param configuration
     * @param DefaultOptions
     */
    private void buildPointerFilterComponents(DatasetConfig configuration) {

        for (FilterPage page : configuration.getFilterPages())

            if (!page.isHidden() && !page.isHideDisplay())

            {
                for (FilterGroup group : page.getFilterGroups())

                    if (!group.isHidden() && !group.isHideDisplay())

                    {
                        for (FilterCollection collection : group.getFilterCollections())

                            for (FilterDescription desc : collection.getFilterDescriptions())

                                if ((desc.getInternalName().equals(filterDescription.getPointerFilter())) && (!desc.isHideDisplay())) {

                                    this.renderPanel = true;

                                    // WARN: This code is for solving bugs in xml config avoiding build combos
                                    // where it is not the case
                                    // SOLUTION is check if component receives pushactions then it is a
                                    // combo component!

                                    if (parentCollection.getFilterConfigurationPage().
                                            getDefaultSelecComposData().get(filterDescription.getPointerFilter()) != null) {

                                        desc.setStyle("menu");

                                        desc.setDisplayType("list");
                                    }

                                    filterDescription = desc;

                                    buildComponent();
                                }
                    }
            }
    }

    /**
     * Builds components for the filterDescription
     */
    private void buildComponent() {

        String displayType = filterDescription.getDisplayType();

        boolean multipleValues = filterDescription.getMultipleValues() == 1;

        String style = filterDescription.getStyle();

        String graph = filterDescription.getGraph();

        FilterComponent child = null;

        if (displayType.equals("container")) {

            String displayStyleOption = filterDescription.getOptions().get(0).getDisplayType();

            int multipleValuesOption = filterDescription.getOptions().get(0).getMultipleValues();

            FilterComponent componentParent = new FilterSelectComponent(filterDescription, this);

            Option filterOptions = filterDescription.getOptions().get(0);


            if (displayStyleOption.equals("list")) {

                if (multipleValuesOption == 1) {

                    child = new FilterCheckBoxComponent(filterOptions);

                } else {

                    child = new FilterRadioComponent(filterOptions);

                }
            } else if (displayStyleOption.equals("text")) {

                child = new FilterTextComponent(filterOptions);
            }

            componentParent.addChildComponent(child);

            filterComponent = componentParent;

        } else {
            if (displayType.equals("list")) {

                if (style.equals("menu") && (graph == null || !graph.equals("1"))) {

                    FilterSelectComponent selecComponent = new FilterSelectComponent(filterDescription, this);

                    //Retrieve PushAction Data from default option
                    parentCollection.getFilterConfigurationPage().storeSelecComponentsDefaultData(selecComponent.getPushActionData_defaultOption());

                    filterComponent = selecComponent;
                } else if (style.equals("menu") && graph != null && graph.equals("1")) {
                    filterComponent = new FilterTextComponent(filterDescription, this);
                } else if (multipleValues) {
                    filterComponent = new FilterCheckBoxComponent(filterDescription, this);
                } else {
                    filterComponent = new FilterRadioComponent(filterDescription, this);
                }
            } else if (displayType.equals("text")) {
                filterComponent = new FilterTextComponent(filterDescription, this);
            } else {
                System.out.println("Component " + filterDescription.getInternalName() + "has not been builded");
                return;
            }
        }


        currentHeight = filterComponent.getCurrentHeight();

        this.removeAll();

        //this.setLayout(new GridLayout(child != null? 2 : 1, 1));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(filterComponent);

        if (child != null) {

            this.add(child);
            currentHeight += child.getCurrentHeight();
        }

        validate();
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 396, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 298, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public FilterDescription getFilterDescription() {
        return filterDescription;
    }

    public final FilterCollectionPanel getParentCollection() {
        return parentCollection;
    }

    public List<Filter> getFilters() {
        return filterComponent.getFilters();
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public void setFilterComponents(FilterComponent components) {
        this.filterComponent = components;
    }

    /**
     * @noinspection UnusedDeclaration
     */

    public Boolean isChild() {
        return (filterDescription == null);
    }

    Integer getCurrentHeight() {
        return currentHeight;
    }

    public Boolean getRenderPanel() {
        return renderPanel;
    }

    public void setRenderPanel(Boolean rendered) {
        this.renderPanel = rendered;
    }

    public Boolean getRenderLabel() {
        return renderLabel;
    }

    public void setRenderLabel(Boolean renderLabel) {
        this.renderLabel = renderLabel;
    }


}

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
package org.gitools.ui.app.datasources.biomart.filter;


import org.gitools.datasources.biomart.restful.model.Filter;
import org.gitools.datasources.biomart.restful.model.FilterDescription;
import org.gitools.datasources.biomart.restful.model.Option;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FilterCheckBoxComponent extends FilterComponent {

    private final Integer CHECK_HEIGHT = 45;

    public FilterCheckBoxComponent(FilterDescription d, FilterDescriptionPanel descriptionParent) {

        super(d, descriptionParent);
        initComponents();

        buildComponent();
    }

    public FilterCheckBoxComponent(Option o) {

        super(o);
        initComponents();

        buildComponent();
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
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 45, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    //FIXME : Test initialisation String list of options
    private void buildComponent() {

        //Retrieve list of text from options
        String[] options = getListTextOptions();

        if (options != null) {
            JCheckBox check = null;
            this.setLayout(new GridLayout(options.length, 1));

            for (String option : options) {
                check = new JCheckBox(option);
                this.add(check);
            }
            currentHeight = CHECK_HEIGHT * options.length;
        }

    }


    @Override
    // FIXME : Check if get filter from check value/s is correct
    public List<Filter> getFilters() {

        List<Filter> filters = new ArrayList<>();

        Filter f = new Filter();

        // Could happen filterDescription null, if this component is a child (belongs to a container component)
        if (filterDescription != null && filterDescription.getInternalName() != null) {
            f.setName(filterDescription.getInternalName());
        }

        for (JCheckBox checkBox : (JCheckBox[]) this.getComponents())
            if (checkBox.isSelected()) {
                f.setValue(checkBox.getText());
            }


        filters.add(f);

        return filters;

    }


    @Override
    //Always render filter from select component filter
    public Boolean hasChanged() {
        return true;

    }

    @Override
    public void setListOptions(List<Option> optionList) {
    }

}

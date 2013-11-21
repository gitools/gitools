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

import org.gitools.datasources.biomart.restful.model.FilterDescription;
import org.gitools.datasources.biomart.restful.model.Option;

import javax.swing.*;
import java.util.List;

public abstract class FilterComponent extends JPanel implements IFilterComponent {

    private FilterComponent childComponent;


    final FilterDescriptionPanel parentPanel;


    final FilterDescription filterDescription;


    final Option filterOptions;

    Integer currentHeight;


    FilterComponent(FilterDescription d, FilterDescriptionPanel parent) {
        filterDescription = d;
        parentPanel = parent;
        filterOptions = null;
    }

    FilterComponent(Option option) {
        filterDescription = null;
        parentPanel = null;
        filterOptions = option;

    }


    @Override
    public Boolean hasChild() {
        return (childComponent != null);
    }

    @Override
    public FilterComponent getChildComponent() {
        return childComponent;
    }

    @Override
    public void addChildComponent(FilterComponent childComponent) {
        this.childComponent = childComponent;
    }


    @Override
    public FilterDescriptionPanel getDescriptionPanel() {
        return parentPanel;
    }

    @Override
    public Integer getCurrentHeight() {
        return currentHeight;
    }


    FilterDescription getFilterDescription() {
        return filterDescription;
    }

    protected String[] getListTextOptions() {
        String res[] = null;
        if (filterOptions != null) {
            res = new String[filterOptions.getOptions().size()];
            for (int i = 0; i < filterOptions.getOptions().size(); i++) {
                res[i] = filterOptions.getOptions().get(i).getValue();
            }

        } else {
            if (filterDescription == null) {
                return res;
            }

            res = new String[filterDescription.getOptions().size()];

            for (int i = 0; i < filterDescription.getOptions().size(); i++) {
                res[i] = filterDescription.getOptions().get(i).getValue();
            }
        }
        return res;
    }

    /**
     * Set options model of a comboBox component.
     * This operation is used for implement the PushAction
     * mechanism
     */
    protected abstract void setListOptions(List<Option> optionList);
}

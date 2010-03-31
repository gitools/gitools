/*
 *  Copyright 2010 xavier.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.gitools.ui.biomart.filter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;

public class FilterDescriptionPanel extends JPanel {

    private FilterCollectionPanel parentCollection;
    private FilterDescription filterDescription;
    private IFilterComponent filterComponent;

    public FilterDescriptionPanel(FilterDescription description, FilterCollectionPanel collectionParent) {

        this.filterDescription = description;
        this.parentCollection = collectionParent;

        buildComponent();
    }

    public FilterDescription getFilterDescription() {
        return filterDescription;
    }

    public FilterCollectionPanel getParentCollection() {
        return parentCollection;
    }

    public Filter getFilter() {
        return filterComponent.getFilter();
    }

    public void setFilterComponents(IFilterComponent components) {
        this.filterComponent = components;
    }

    public Boolean isChild() {
        return (filterDescription == null);
    }

    private void buildComponent() {

        String displayType = filterDescription.getDisplayType();
        String multipleValues = filterDescription.getMultipleValues();
        String style = filterDescription.getStyle();
        String graph = filterDescription.getGraph();

        if (displayType.equals("container")) {

            String displayStyleOption = filterDescription.getOptions().get(0).getDisplayType();
            String multipleValuesOption = filterDescription.getOptions().get(0).getMultipleValues();

            IFilterComponent componentParent = new FilterSelecComponent(filterDescription, this);
            Option filterOptions = filterDescription.getOptions().get(0);
            IFilterComponent child = null;

            if (displayStyleOption.equals("list")) {
                if (multipleValuesOption.equals("1")) {
                    child = new FilterCheckBoxComponent(filterOptions, componentParent);
                } else {
                    child = new FilterRadioComponent(filterOptions, componentParent);

                }
            } else if (displayStyleOption.equals("text")) {
                child = new FilterTextComponent(filterOptions, componentParent);
            }

            componentParent.addChildComponent(child);
            filterComponent = componentParent;

        } else if (displayType.equals("list")) {

            if (style.equals("menu") && !graph.equals("1")) {
                filterComponent = new FilterSelecComponent(filterDescription, this);
            } else if (style.equals("menu") && graph.equals("1")) {
                filterComponent = new FilterTextComponent(filterDescription, this);
            } else if (multipleValues.equals("1")) {
                filterComponent = new FilterCheckBoxComponent(filterDescription, this);
            } else {
                filterComponent = new FilterRadioComponent(filterDescription, this);
            }

        } else if (displayType.equals("text")) {

            filterComponent = new FilterTextComponent(filterDescription, this);
        }

    }
}

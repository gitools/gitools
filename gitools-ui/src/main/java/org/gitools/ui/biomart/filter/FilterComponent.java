/*
 *  Copyright 2010 xavi.
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

import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;

abstract class FilterComponent implements IFilterComponent {

    protected IFilterComponent childComponent;
    protected IFilterComponent parentComponent;
    protected FilterDescriptionPanel parentPanel;
    protected FilterDescription filterDescription;
    protected Option filterOptions;

    FilterComponent(FilterDescription d, FilterDescriptionPanel parent) {
        filterDescription = d;
        parentPanel = parent;
    }

    FilterComponent(Option o, IFilterComponent c) {
        filterDescription = null;
        parentPanel = null;
        filterOptions = o;
        parentComponent = c;
    }


    @Override
    public Boolean hasChild() {
        return (childComponent != null);
    }

    @Override
    public IFilterComponent getChildComponent() {
        return childComponent;
    }

    @Override
    public void addChildComponent(IFilterComponent childComponent) {
        this.childComponent = childComponent;
    }

    @Override
    public FilterDescriptionPanel getDescriptionPanel() {
        return parentPanel;
    }
}

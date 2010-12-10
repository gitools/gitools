/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

import java.util.List;
import javax.swing.JPanel;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;

public abstract class FilterComponent extends JPanel implements IFilterComponent {

    protected FilterComponent childComponent;

	protected FilterDescriptionPanel parentPanel;

	protected FilterDescription filterDescription;

    protected Option filterOptions; 

	protected Integer currentHeight;


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

	public FilterDescription getFilterDescription() {
		return filterDescription;
	}

	/**
	 * Set options model of a comboBox component.
	 * This operation is used for implement the PushAction
	 * mechanism
	 */
	public abstract void setListOptions(List<Option> optionList);
}

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
package org.gitools.biomart.filtermanagement;

import javax.swing.JPanel;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;

public abstract class FilterDescriptionPanel extends JPanel implements IFilterComponent {

	protected FilterDescription filterDescription;
	protected FilterCollectionPanel parentCollection;

	// A FilterDescriptionPanel may have a IFilterComponent child. This
	// will have Option instead of FilterDescription
	protected IFilterComponent parent;
	protected Option filterOptions;

	public FilterDescriptionPanel(FilterDescription description, FilterCollectionPanel collectionParent) {

		this.filterDescription = description;
		this.parentCollection = collectionParent;
		this.parent = null;
		this.filterOptions = null;
	}

	public FilterDescriptionPanel(Option o, IFilterComponent parentComponent) {
		
		this.filterDescription = null;
		this.parentCollection = null;
		this.parent = parentComponent;
		this.filterOptions = o;

	}

	public FilterDescription getFilterDescription() {
		return filterDescription;
	}

	@Override
	public FilterCollectionPanel getParentCollection() {
		return parentCollection;
	}

	public Boolean isChild(){
		return (filterDescription == null);
	}

	public Boolean hasChild(){
		return (parent != null);
	}

}

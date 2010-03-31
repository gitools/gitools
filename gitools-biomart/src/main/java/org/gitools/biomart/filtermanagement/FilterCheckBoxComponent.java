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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;

class FilterCheckBoxComponent extends FilterDescriptionPanel {


	private List<JCheckBox> checkBoxComponents;
	private String component;

	FilterCheckBoxComponent(FilterDescription d,FilterCollectionPanel collectionParent) {

		super(d,collectionParent);
		buildComponent();
	}

	FilterCheckBoxComponent(Option o, IFilterComponent parentComponent) {

		super(o, parentComponent);
		buildComponent();
	}

//FIXME : Test initialisation String list of options
	private void buildComponent() {

		String[] options = getListTextOptions();
		checkBoxComponents = new ArrayList<JCheckBox>();

		for (int i =0; i < options.length; i++){
			checkBoxComponents.add(new JCheckBox(options[i],(i==0) ? true : false));
			checkBoxComponents.get(i).setVisible(true);
		}

	}

	@Override
	// FIXME : Check if get filter from radio value/s in list is correct
	public Filter getFilter() {

		Filter f = new Filter();
		f.setName(filterDescription.getInternalName());

		for (JCheckBox r : checkBoxComponents)
			if (r.isSelected()) 
				f.setValue(r.getText());
			

		return f;
	}

	@Override
	//Always render filter from select component filter
	public Boolean hasChanged() {
		return true;

	}

	/**
	 * If the component is child the text of each radio is obtained from filterOptions
	 * component
	 * @param child
	 * @return
	 */
	private String[] getListTextOptions() {
		String res[];
		if (isChild()) {
			res = new String[filterOptions.getOptions().size()];
			for (int i = 0; i < filterOptions.getOptions().size(); i++) {
				res[i] = filterOptions.getOptions().get(i).getValue();
			}

		} else {
			res = new String[filterDescription.getOptions().size()];

			for (int i = 0; i < filterDescription.getOptions().size(); i++) {
				res[i] = filterDescription.getOptions().get(i).getValue();
			}
		}
		return res;
	}

}

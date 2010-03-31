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
import javax.swing.JRadioButton;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;

class FilterRadioComponent extends FilterComponent {

	private List<JRadioButton> radioComponents;

	FilterRadioComponent(FilterDescription d, FilterDescriptionPanel descriptionParent) {

		super(d, descriptionParent);
		buildComponent();
	}

	FilterRadioComponent(Option o, IFilterComponent parentComponent) {

		super(o, parentComponent);
		buildComponent();
	}

//FIXME : Test initialisation String list of options
	private void buildComponent() {

		String[] options;

		//Retrieve list of text from options
		options = getListTextOptions();
		radioComponents = new ArrayList<JRadioButton>();

		for (int i = 0; i < options.length; i++) {
			radioComponents.add(new JRadioButton(options[i], (i == 0) ? true : false));
			radioComponents.get(i).setVisible(true);
		}

	}

	@Override
	// FIXME : Check if get filter from radio value/s is correct
	public Filter getFilter() {

		Filter f = new Filter();
		f.setName(filterDescription.getInternalName());
		for (JRadioButton r : radioComponents) {
			if (r.isSelected()) {

				if (r.getText().toLowerCase().equals("excluded")) {
					f.setRadio(true);
					f.setValue("1");
				} else {
					if (r.getText().toLowerCase().equals("only")) {
						f.setValue("0");
						f.setRadio(true);
					} else {
						f.setValue(r.getText());
					}
				}
			}
		}

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
		if (hasChild()) {
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

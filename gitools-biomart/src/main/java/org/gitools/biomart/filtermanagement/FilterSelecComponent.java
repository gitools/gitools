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

import javax.swing.JComboBox;
import javax.swing.JList;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterDescription;


public class FilterSelecComponent extends FilterDescriptionPanel {


	private JComboBox comboComponent;
	private JList listComponent;
	private FilterCollectionPanel parent;
	private String component;

	FilterSelecComponent(FilterDescription d,FilterCollectionPanel collectionParent) {

		super(d,collectionParent);
		buildComponent();
	}

//FIXME : Test initialisation String list of options
	private void buildComponent() {

		String[] options = InitListOptions();
		if (filterDescription.getMultipleValues() == null || !filterDescription.getMultipleValues().equals("1")) {

			component = "ComboBox";
			comboComponent= new JComboBox(options);
			comboComponent.setVisible(true);

		} else {

			component = "List";
			listComponent = new JList(options);
			listComponent.setVisible(true);
		}

	}

	@Override
	// FIXME : Check get Filter for selected value/s in list
	public Filter getFilter() {

		Filter f = new Filter();
		f.setName(filterDescription.getInternalName());

		if (component.equals("ComboBox"))
			f.setValue(comboComponent.getSelectedItem().toString());
		else{
			String selStr = "";
			for (String res: (String[]) listComponent.getSelectedValues())
				selStr = selStr + "," + res;

			f.setValue(selStr);
		}
		return f;
	}

	@Override
	//Always render filter from select component filter
	public Boolean hasChanged() {
		return true;

	}

	private String[] InitListOptions() {
		String res[] = new String[filterDescription.getOptions().size()];
		for (int i =0;i < filterDescription.getOptions().size(); i++)
			res[i] = filterDescription.getOptions().get(i).getValue();

		return res;
	}


}

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
import javax.swing.JPanel;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.restful.model.FilterCollection;
import org.gitools.biomart.restful.model.FilterDescription;

import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.Option;

public class FilterCollectionPanel extends JPanel {

	private String labelPanel;
	private List<IFilterComponent> components;

	public FilterCollectionPanel(FilterCollection fc) throws BiomartServiceException {

		labelPanel = fc.getDisplayName();
		components = new ArrayList<IFilterComponent>();

		for (FilterDescription d : fc.getFilterDescriptions()) {
			if (!d.getHideDisplay().equals("true"))
				components.addAll(createComponents(d));
		}

	}

	public List<Filter> getFilters() {

		List<Filter> lf = new ArrayList<Filter>();

		for (IFilterComponent c : components)
		{
			lf.add(c.getFilter());
		}
		return lf;
	}

	public List<IFilterComponent> getCompo() {
		return components;
	}

	public void setCompos(List<IFilterComponent> compos) {
		this.components = compos;
	}

	public String getLabelPanel() {
		return labelPanel;
	}

	public void setLabelPanel(String labelPanel) {
		this.labelPanel = labelPanel;
	}

	private List<IFilterComponent> createComponents(FilterDescription d) {

		List<IFilterComponent> listComponents = new ArrayList<IFilterComponent>();

		String displayType = d.getDisplayType();
		String multipleValues = d.getMultipleValues();
		String style = d.getStyle();
		String graph = d.getGraph();

		if (displayType.equals("container")) {

			String displayStyleOption = d.getOptions().get(0).getDisplayType();
			String multipleValuesOption = d.getOptions().get(0).getMultipleValues();

			IFilterComponent componentParent = new FilterSelecComponent(d,this);
			listComponents.add(componentParent);

			Option filterOptions = d.getOptions().get(0);

			if (displayStyleOption.equals("list")) {
				if (multipleValuesOption.equals("1")) {
					listComponents.add(new FilterCheckBoxComponent(filterOptions,componentParent));
				} else {
					listComponents.add(new FilterRadioComponent(filterOptions,componentParent));
				}
			} else if (displayStyleOption.equals("text")) {
				listComponents.add(new FilterTextComponent(filterOptions,componentParent));
			}

		} else if (displayType.equals("list")) {

			if (style.equals("menu") && !graph.equals("1")) {
				listComponents.add(new FilterSelecComponent(d,this));
			} else if (style.equals("menu") && graph.equals("1")) {
				listComponents.add(new FilterTextComponent(d,this));
			} else if (multipleValues.equals("1")) {
				listComponents.add(new FilterCheckBoxComponent(d,this));
			} else {
				listComponents.add(new FilterRadioComponent(d,this));
			}

		} else if (displayType.equals("text")) {

			listComponents.add(new FilterTextComponent(d,this));
		}
		return listComponents;
	}


}

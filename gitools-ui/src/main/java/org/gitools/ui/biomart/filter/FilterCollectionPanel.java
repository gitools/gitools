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
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.restful.model.FilterCollection;
import org.gitools.biomart.restful.model.FilterDescription;

import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.Option;

public class FilterCollectionPanel extends JPanel {

	private JLabel labelCollectionPanel;
	private List<FilterDescriptionPanel> descriptionPanels;

	public FilterCollectionPanel(FilterCollection fc) throws BiomartServiceException {

		labelCollectionPanel = new JLabel(fc.getDisplayName());
		descriptionPanels = new ArrayList<FilterDescriptionPanel>();

		for (FilterDescription d : fc.getFilterDescriptions()) {
			if (!d.getHideDisplay().equals("true"))
				descriptionPanels.add(new FilterDescriptionPanel(d,this));
		}

	}

	public List<Filter> getFilters() {

		List<Filter> lf = new ArrayList<Filter>();

		for (FilterDescriptionPanel d : descriptionPanels)
		{                   
                        lf.add(d.getFilter());
		}
		return lf;
	}


	public String getLabelPanel() {
		return labelCollectionPanel.getText();
	}

	public void setLabelPanel(String labelPanel) {
		this.labelCollectionPanel.setText(labelPanel);
	}


}

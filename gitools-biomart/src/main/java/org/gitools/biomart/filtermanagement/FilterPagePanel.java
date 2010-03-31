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

import org.gitools.biomart.restful.model.FilterGroup;
import org.gitools.biomart.restful.model.FilterPage;

public class FilterPagePanel extends JPanel {

	private String labelPanel;
	private List<FilterGroupPanel> groups;

	public FilterPagePanel(FilterPage fp) throws BiomartServiceException {

		labelPanel = fp.getDisplayName();
		groups = new ArrayList<FilterGroupPanel>();


		FilterGroupPanel groupFilter = null;

		for (FilterGroup g : fp.getFilterGroups()){
				groupFilter = new FilterGroupPanel(g,fp);
				groups.add(groupFilter);
		}

	}


	public List<FilterGroupPanel> getGroups() {
		return groups;
	}

	public String getLabelPanel() {
		return labelPanel;
	}


}

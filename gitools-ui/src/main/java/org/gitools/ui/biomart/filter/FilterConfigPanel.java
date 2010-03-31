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
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.restful.model.DatasetConfig;

import org.gitools.biomart.restful.model.FilterGroup;
import org.gitools.biomart.restful.model.FilterPage;

public class FilterConfigPanel extends JPanel {

	private String labelPanel;
	private List<FilterPagePanel> pages;

	public FilterConfigPanel(DatasetConfig conf) throws BiomartServiceException {

		labelPanel = conf.getDisplayName();
		pages = new ArrayList<FilterPagePanel>();


		FilterPagePanel pageFilter = null;

		for (FilterPage p : conf.getFilterPages()){
				pageFilter = new FilterPagePanel(p);
				pages.add(pageFilter);
		}

	}


	public List<FilterPagePanel> getPages() {
		return pages;
	}

	public String getLabelPanel() {
		return labelPanel;
	}


}

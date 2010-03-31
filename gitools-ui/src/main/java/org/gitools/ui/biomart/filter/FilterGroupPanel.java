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
import org.gitools.biomart.restful.model.FilterCollection;

import org.gitools.biomart.restful.model.FilterGroup;
import org.gitools.biomart.restful.model.FilterPage;

public class FilterGroupPanel extends JPanel {

	private String labelPanel;
	private List<FilterCollectionPanel> collections;
	private FilterPage pageParent;

	public FilterGroupPanel(FilterGroup fg,FilterPage parent) throws BiomartServiceException {

		labelPanel = fg.getDisplayName();
		pageParent = parent;
		collections = new ArrayList<FilterCollectionPanel>();


		FilterCollectionPanel collectionFilter = null;
		for (FilterCollection c : fg.getFilterCollections()){
				collectionFilter = new FilterCollectionPanel(c);
				collections.add(collectionFilter);
		}

	}


	public List<FilterCollectionPanel> getCollections() {
		return collections;
	}

	public String getLabelPanel() {
		return labelPanel;
	}

	public FilterPage getPageParent() {
		return pageParent;
	}

}

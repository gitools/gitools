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

import java.util.List;

import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.restful.model.Filter;

import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.FilterCollection;
import org.gitools.biomart.restful.model.FilterGroup;
import org.gitools.biomart.restful.model.FilterPage;

public class FilterManager {

	private FilterManager instance;

	public FilterManager getInstance() {
		if (instance == null) {
			instance = new FilterManager();
		}

		return instance;
	}

	private FilterManager() {
	}

	/**
	 * Retrieves a Filter object from a panel.
	 * The Filter Object permits to build the Query
	 * @param panel
	 * @return
	 */
	public List<Filter> getBiomartFilters(FilterCollectionPanel panel) {
		return panel.getFilters();

	}

	/**
	 * Given a FilterCollection and a group produces the associated panel
	 * @param fpg
	 * @param fd
	 * @param groupName
	 * @param collectionName
	 * @return
	 */
	public FilterCollectionPanel createBiomartFilterPanel(DatasetConfig ds,
			String groupName, String collectionName) throws BiomartServiceException {

		FilterCollection fc = getFilterCollection(ds, groupName, collectionName);
		FilterCollectionPanel panel = new FilterCollectionPanel(fc);

		return panel;
	}

	/**
	 * Retrieve FilterCollection
	 * @param ds
	 * @param groupName
	 * @param collectionName
	 * @return
	 * @throws BiomartServiceException
	 */
	private FilterCollection getFilterCollection(DatasetConfig ds, String groupName, String collectionName) throws BiomartServiceException {
		for (FilterPage f : ds.getFilterPages()) {
			if (f.getHideDisplay() == null || f.getHideDisplay().equals("")
					|| !f.getHideDisplay().equals("true")) {

				for (FilterGroup g : f.getFilterGroups()) {
					if (g.getInternalName().equals(groupName)) {
						for (FilterCollection c : g.getFilterCollections()) {
							if (c.getInternalName().equals(collectionName)) {
								return c;
							}
						}
					}
				}
			}

		}
		throw new BiomartServiceException("No Filter collection found for input options");
	}
}

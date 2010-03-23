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
import org.gitools.biomart.soap.model.Filter;
import org.gitools.biomart.restful.model.FilterCollection;

/**
 *
 * @author xavier
 */
class CheckBoxListBiomartFilterPanel implements IBiomartFilterPanel {

	public CheckBoxListBiomartFilterPanel(FilterCollection fc) {
	}


	@Override
	public List<Filter> getFilters() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setFilterValue(String val) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<String> getFilterNames() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getFilterValue(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}

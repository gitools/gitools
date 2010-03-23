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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gitools.biomart.soap.model.Filter;
import org.gitools.biomart.restful.model.FilterCollection;
import org.gitools.biomart.restful.model.FilterDescription;

/**
 *
 * @author xavier
 */
public class SelectBiomartFilterPanel implements IBiomartFilterPanel {

	private final Integer textAreacols = 25;
	private final Integer textArearows = 4;
	private final Integer fileFieldSize = 16;
	private String labelPanel;
	//Values selected
	private String nameCompo;
	private String valueCompo;

	private Boolean isMultipleSelec;
	
	//Options
	private Map<String,String> optionsLblName;


	SelectBiomartFilterPanel(FilterCollection fc) {

		isMultipleSelec = fc.getFilterDescriptions().get(0).getMultipleValues().equals("1");
		labelPanel = fc.getDisplayName();

		nameCompo = fc.getFilterDescriptions().get(0).getInternalName();
		optionsLblName = new HashMap<String, String>();
		for (FilterDescription d :fc.getFilterDescriptions())
			optionsLblName.put(d.getDisplayName(), d.getInternalName());

	}

	public Boolean isMultipleSelec() {
		return isMultipleSelec;
	}
	@Override
	public void setFilterValue(String val) {
		valueCompo = val;
	}


	@Override
	public List<Filter> getFilters() {
		List<Filter> lf = new ArrayList<Filter>();
		Filter f = new Filter();
		f.setName(nameCompo);
		f.setValue(valueCompo);

		lf.add(f);
		return lf;
	}

	public Map<String, String> getOptionsLblName() {
		return optionsLblName;
	}

	public void setOptionsLblName(Map<String, String> optionsLblName) {
		this.optionsLblName = optionsLblName;
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

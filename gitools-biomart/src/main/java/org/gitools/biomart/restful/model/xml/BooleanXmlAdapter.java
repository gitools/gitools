/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.biomart.restful.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class BooleanXmlAdapter extends XmlAdapter<Integer, Boolean> {

	@Override
	public Boolean unmarshal(Integer v) throws Exception {
		return v != null && v.intValue() == 1;
	}

	@Override
	public Integer marshal(Boolean v) throws Exception {
		return v ? 1 : 0;
	}

}

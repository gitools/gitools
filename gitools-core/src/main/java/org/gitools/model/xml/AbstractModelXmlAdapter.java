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

package org.gitools.model.xml;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.AbstractModel;

@Deprecated //FIXME Esta clase tiene muy mala pinta !!!!!!!!!!!
public class AbstractModelXmlAdapter
		extends XmlAdapter<AbstractModelDecoratorElement, List<AbstractModel>> {
	
	@Override
	public AbstractModelDecoratorElement marshal(List<AbstractModel> elems)
			throws Exception {
		return new AbstractModelDecoratorElement(elems);
	}

	@Override
	public List<AbstractModel> unmarshal(AbstractModelDecoratorElement v)
			throws Exception {
		 return v.getList();
	}

}

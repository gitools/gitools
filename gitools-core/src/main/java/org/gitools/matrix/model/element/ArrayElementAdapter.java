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

package org.gitools.matrix.model.element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArrayElementAdapter 
		extends AbstractElementAdapter {

	private static final long serialVersionUID = 5864596809781257355L;

	private static class ArrayElementProperty extends AbstractElementAttribute {

		private static final long serialVersionUID = 7803752573190009823L;

		public ArrayElementProperty(String id, String name, String description) {
			super(id, name, description, double.class);
		} 
	}
	
	protected String[] ids;
	
	protected ArrayElementAdapter() {
	}
	
	public ArrayElementAdapter(String[] ids) {
		super(double[].class);
		
		this.ids = ids;
		
		List<IElementAttribute> properties = new ArrayList<IElementAttribute>();
		
		for (String id : ids)
			properties.add(new ArrayElementProperty(id, id, ""));

		setProperties(properties);
	}
	
	@Override
	public Object getValue(Object element, int index) {
		return element != null ? ((double[])element)[index] : null;
	}

	@Override
	public void setValue(Object element, int index, Object value) {
		if (element != null)
			((double[])element)[index] = (Double) value;
	}
}

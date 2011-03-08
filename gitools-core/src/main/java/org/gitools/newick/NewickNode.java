/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.newick;

import java.util.ArrayList;
import java.util.List;

public class NewickNode<VT> {

	private String name;
	private VT value;

	private List<NewickNode> children;

	public NewickNode() {
		this(null, null);
	}

	public NewickNode(String name, VT value) {
		this.name = name;
		this.value = value;
		this.children = new ArrayList<NewickNode>(2);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VT getValue() {
		return value;
	}

	public void setValue(VT value) {
		this.value = value;
	}

	public NewickNode getChild(int index) {
		return children.get(index);
	}

	public void setChild(int index, NewickNode<VT> node) {
		children.set(index, node);
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!children.isEmpty()) {
			sb.append('(').append(children.get(0));
			for (int i = 1; i < children.size(); i++)
				sb.append(',').append(children.get(i));
			sb.append(')');
		}
		if (name != null)
			sb.append(name);
		if (value != null)
			sb.append(":").append(value);
		return sb.toString();
	}
}

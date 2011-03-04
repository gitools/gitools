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

public class NewickNode<VT> {

	private String name;
	private VT value;

	private NewickNode leftChild;
	private NewickNode rightChild;

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

	public NewickNode getLeftChild() {
		return leftChild;
	}

	public NewickNode getRightChild() {
		return rightChild;
	}

	public NewickNode getChild(int index) {
		switch (index) {
			case 0: return leftChild;
			case 1: return rightChild;
			default: return null;
		}
	}

	public void setChild(int index, NewickNode<VT> node) {
		switch (index) {
			case 0: leftChild = node;
			case 1: rightChild = node;
		}
	}

	public boolean isLeaf() {
		return leftChild == null && rightChild == null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (isLeaf()) {
			sb.append(name);
			if (value != null)
				sb.append(":").append(value);
		}
		else {
			sb.append(leftChild);
			sb.append(',');
			sb.append(rightChild);
		}
		return sb.toString();
	}
}

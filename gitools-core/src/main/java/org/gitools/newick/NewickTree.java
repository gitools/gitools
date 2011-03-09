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
import java.util.Vector;

public class NewickTree<VT> {

	private NewickNode<VT> root;

	public NewickTree() {
	}

	public NewickNode<VT> getRoot() {
		return root;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void setRoot(NewickNode<VT> root) {
		this.root = root;
	}

	@Override
	public String toString() {
		return root.toString() + ";";
	}

	// FIXME This method should be implemented in the nodes
	public List<NewickNode<VT>> getLeaves(NewickNode<VT> node) {
		List<NewickNode<VT>> leaves = new ArrayList<NewickNode<VT>>(0);

		if (node != null) {
			if (node.isLeaf())
				leaves.add(node);
			else
				for (NewickNode<VT> n : node.getChildren())
					leaves.addAll(getLeaves(n));

		}
		return leaves;
	}
}

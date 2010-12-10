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

package org.gitools.ui.workspace;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AbstractNode extends DefaultMutableTreeNode implements INavigatorNode {

	private static final long serialVersionUID = 61176815569716011L;

	private boolean expanded;
	
	public AbstractNode() {
		super();
	}
	
	public AbstractNode(Object userObject) {
		super(userObject);
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	@Override
	public void expand() {
		expanded = true;
	}
	
	@Override
	public void refresh() {	
	}
	
	@Override
	public void collapse() {
		expanded = false;
	}
	
	@Override
	public Icon getIcon() {
		return null;
	}
	
	@Override
	public String getLabel() {
		final Object userObject = getUserObject();
		return userObject != null ? userObject.toString() : "";
	}
	
	@Override
	public boolean isLeaf() {
		return isExpanded() ? super.isLeaf() : false;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
}

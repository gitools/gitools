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

package org.gitools.ui.platform.navigator;

public abstract class NavigatorNode {

	private Object object;
	
	public NavigatorNode(Object object) {
		this.object = object;
	}
	
	public Object getObject() {
		return object;
	}
	
	public int getChildCount() {
		return 0;
	}
	
	public NavigatorNode getChild(int index) {
		return null;
	}
	
	public int getIndexOfChild(NavigatorNode child) {
		return 0;
	}
	
	public boolean isLeaf() {
		return true;
	}
	
	public String getLabelText() {
		return object.toString();
	}
	
	@Override
	public String toString() {
		return getLabelText();
	}
}

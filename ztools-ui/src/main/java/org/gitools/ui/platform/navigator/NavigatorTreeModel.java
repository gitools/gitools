package org.gitools.ui.platform.navigator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.gitools.model.Workspace;

public class NavigatorTreeModel implements TreeModel {

	private Workspace workspace;
	
	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>(0);
	
	public NavigatorTreeModel() {
		workspace = new Workspace();
	}
	
	public NavigatorTreeModel(Workspace workspace) {
		this.workspace = workspace;
	}
	
	@Override
	public Object getRoot() {
		return new WorkspaceNode(workspace);
	}
	
	@Override
	public int getChildCount(Object parent) {
		NavigatorNode node = (NavigatorNode) parent;
		return node.getChildCount();
	}
	
	@Override
	public Object getChild(Object parent, int index) {
		NavigatorNode node = (NavigatorNode) parent;
		return node.getChild(index);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		NavigatorNode parentNode = (NavigatorNode) parent;
		NavigatorNode childNode = (NavigatorNode) child;
		return parentNode.getIndexOfChild(childNode);
	}

	@Override
	public boolean isLeaf(Object node) {
		NavigatorNode n = (NavigatorNode) node;
		return n.isLeaf();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}
	
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

}

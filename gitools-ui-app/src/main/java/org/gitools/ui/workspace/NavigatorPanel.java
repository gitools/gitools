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

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

import org.gitools.workspace.Workspace;
import org.gitools.workspace.WorkspaceManager;

public class NavigatorPanel extends JPanel {

	private static final long serialVersionUID = 5667653501673874725L;

	private Workspace workspace;
	
	private JTree tree;
	
	public NavigatorPanel() {
		this.workspace = WorkspaceManager.getDefault().getWorkspace();
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	public NavigatorPanel(Workspace workspace) {
		this.workspace = workspace;
		
		createComponents();
	}

	private void createComponents() {
		//tree = new JTree(new WorkspaceNode(workspace));
		DefaultMutableTreeNode rootNode = null;
		
		File file = new File(System.getProperty("user.dir"));//FIXME
		rootNode = new FileNode(file);
		
		tree = new JTree(rootNode);
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			
			@Override public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				Object node = event.getPath().getLastPathComponent();
				if (node instanceof INavigatorNode)
					((INavigatorNode) node).expand();
			}
			
			@Override public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				Object node = event.getPath().getLastPathComponent();
				if (node instanceof INavigatorNode)
					((INavigatorNode) node).collapse();
			}
		});
		
		tree.getModel().addTreeModelListener(new TreeModelListener() {
			
			@Override
			public void treeStructureChanged(TreeModelEvent e) {
				System.out.println("structure changed" + e.getTreePath());
			}
			
			@Override
			public void treeNodesRemoved(TreeModelEvent e) {
				System.out.println("nodes removed" + e.getTreePath());
			}
			
			@Override
			public void treeNodesInserted(TreeModelEvent e) {
				System.out.println("nodes inserted" + e.getTreePath());
			}
			
			@Override
			public void treeNodesChanged(TreeModelEvent e) {
				System.out.println("nodes changed" + e.getTreePath());
			}
		});

		setLayout(new BorderLayout());
		add(tree);
	}

	public WorkspaceNode getWorkspaceNode() {
		return (WorkspaceNode) tree.getModel().getRoot();
	}
	
	public void refresh() {
		((DefaultTreeModel) tree.getModel()).reload();
	}
}

package org.gitools.ui.platform.navigator;

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

import org.gitools.model.Workspace;

public class NavigatorPanel extends JPanel {

	private static final long serialVersionUID = 5667653501673874725L;

	private Workspace workspace;
	
	private JTree tree;
	
	public NavigatorPanel() {
		File path = new File(System.getProperty("user.home")); //FIXME
		this.workspace = new Workspace(path);
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

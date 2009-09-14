package org.gitools.ui.wizard.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.gitools.ui.wizard.AbstractWizardPage;

public abstract class FilteredTreePage extends AbstractWizardPage {

	protected FilteredTreePanel panel;

	private String lastFilterText = "";
	
	@Override
	public JComponent createControls() {
		panel = new FilteredTreePanel();
		
		panel.filterField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				updateFilterActionPerformed(); }
			@Override public void insertUpdate(DocumentEvent e) {
				updateFilterActionPerformed(); }
			@Override public void removeUpdate(DocumentEvent e) {
				updateFilterActionPerformed(); }
		});
		
		panel.clearBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				panel.filterField.setText("");
				panel.filterField.requestFocusInWindow();
			}
		});
		
		panel.expandBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				expandAll(); }
		});
		
		panel.collapseBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				collapseAll(); }
		});
		
		updateFilterActionPerformed();
		
		return panel;
	}

	private void updateFilterActionPerformed() {
		final String filterText = getFilterText();
		
		panel.clearBtn.setEnabled(!filterText.isEmpty());
		
		if (!filterText.equalsIgnoreCase(lastFilterText)) {
			lastFilterText = filterText;
			panel.tree.setModel(createModel(getFilterText()));
			expandAll();
		}
	}
	
	protected abstract TreeModel createModel(String filterText);

	private String getFilterText() {
		return panel.filterField.getText();
	}
	
	public void setModel(TreeModel model) {
		panel.tree.setModel(model);
	}
	
	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public void expandCollapse(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		
		// Traverse tree from root
		expandCollapse(tree, new TreePath(root), expand);
		
		// Do not collapse first level nodes
		if (!tree.isRootVisible() && !expand)
			tree.expandPath(new TreePath(root));
	}

	private void expandCollapse(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandCollapse(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand)
			tree.expandPath(parent);
		else
			tree.collapsePath(parent);
	}

	public void expandAll() {
		expandCollapse(panel.tree, true);
	}
	
	public void collapseAll() {
		expandCollapse(panel.tree, false);
	}
}

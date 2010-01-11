package org.gitools.ui.dialog;

import org.gitools.ui.platform.dialog.AbstractDialog;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.gitools.ui.platform.dialog.DialogHeaderPanel.Status;

public class ExportDialog extends AbstractDialog {

	private static final long serialVersionUID = -829366063374164258L;

	protected JTree tree;
	
	public ExportDialog(Window owner) {
		super(
				owner,
				"Export", 
				"Select", 
				"Choose export destination", 
				Status.normal, 
				null);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setMinimumSize(new Dimension(500, 300));
	}
	
	@Override
	protected JComponent createContainer() {
		DefaultMutableTreeNode model = 
			new DefaultMutableTreeNode();

		DefaultMutableTreeNode table = new DefaultMutableTreeNode("Table");
		table.add(new DefaultMutableTreeNode("Header names"));
		table.add(new DefaultMutableTreeNode("One parameter table"));
		table.add(new DefaultMutableTreeNode("Many parameters table"));
		model.add(table);
		
		DefaultMutableTreeNode picture = new DefaultMutableTreeNode("Picture");
		picture.add(new DefaultMutableTreeNode("PDF document"));
		picture.add(new DefaultMutableTreeNode("Image file (png, jpg, ...)"));
		model.add(picture);
		
		DefaultTreeCellRenderer renderer = 
			new DefaultTreeCellRenderer();
		
		tree = new JTree(model);
		tree.setRootVisible(false);
		tree.setCellRenderer(renderer);
		
		final JScrollPane scrollPane = new JScrollPane(tree);
		return scrollPane;
	}
	
	@Override
	protected List<JButton> createButtons() {
		List<JButton> buttons= new ArrayList<JButton>();
		buttons.add(new JButton("Accept"));
		buttons.add(new JButton("Cancel"));
		return buttons;
	}
}

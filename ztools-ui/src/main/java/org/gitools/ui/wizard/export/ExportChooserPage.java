package org.gitools.ui.wizard.export;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.gitools.ui.wizard.AbstractWizardPage;

public class ExportChooserPage extends AbstractWizardPage {

	private static final long serialVersionUID = 873494691491409555L;
	
	protected JTree tree;
	
	public ExportChooserPage(String id) {
		super(id);
	}
	
	public ExportChooserPage() {
		super(ExportChooserPage.class.getSimpleName());
	}
	
	@Override
	public JComponent createControls() {
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
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
		return scrollPane;
	}
}

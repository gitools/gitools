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

package org.gitools.ui.wizard.export;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

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

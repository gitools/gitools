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

import org.gitools.ui.platform.dialog.MessageStatus;

public class ExportDialog extends AbstractDialog {

	private static final long serialVersionUID = -829366063374164258L;

	protected JTree tree;
	
	public ExportDialog(Window owner) {
		super(
				owner,
				"Export", 
				"Select", 
				"Choose export destination", 
				MessageStatus.INFO,
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

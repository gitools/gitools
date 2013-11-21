/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.wizard.export;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @noinspection ALL
 */
public class ExportChooserPage extends AbstractWizardPage {

    private static final long serialVersionUID = 873494691491409555L;

    private JTree tree;

    private ExportChooserPage(String id) {
        super(id);
    }

    public ExportChooserPage() {
        super(ExportChooserPage.class.getSimpleName());
    }


    @Override
    public JComponent createControls() {
        DefaultMutableTreeNode model = new DefaultMutableTreeNode();

        DefaultMutableTreeNode table = new DefaultMutableTreeNode("Table");
        table.add(new DefaultMutableTreeNode("Header names"));
        table.add(new DefaultMutableTreeNode("One parameter table"));
        table.add(new DefaultMutableTreeNode("Many parameters table"));
        model.add(table);

        DefaultMutableTreeNode picture = new DefaultMutableTreeNode("Picture");
        picture.add(new DefaultMutableTreeNode("PDF document"));
        picture.add(new DefaultMutableTreeNode("Image file (png, jpg, ...)"));
        model.add(picture);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        tree = new JTree(model);
        tree.setRootVisible(false);
        tree.setCellRenderer(renderer);

        final JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        return scrollPane;
    }
}

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
package org.gitools.ui.dialog;

import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExportDialog extends AbstractDialog
{

    private static final long serialVersionUID = -829366063374164258L;

    protected JTree tree;

    public ExportDialog(Window owner)
    {
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

    @NotNull
    @Override
    protected JComponent createContainer()
    {
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

    @NotNull
    @Override
    protected List<JButton> createButtons()
    {
        List<JButton> buttons = new ArrayList<JButton>();
        buttons.add(new JButton("Accept"));
        buttons.add(new JButton("Cancel"));
        return buttons;
    }
}

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

import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.ui.heatmap.header.wizard.coloredlabels.ColoredLabelsGroupsPage;
import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditCategoricalScaleDialog extends AbstractDialog {


    private final ColoredLabelsGroupsPage page;

    public EditCategoricalScaleDialog(Window owner, ColoredLabel[] coloredLabels) {
        super(owner, "Edit Categorical Scale", "Edit Categorical Scale", "Edit Categorical Scale", MessageStatus.INFO, null);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setMinimumSize(new Dimension(500, 500));

        this.page = new ColoredLabelsGroupsPage(coloredLabels);
        setContainer(this.page);
        page.setValueEditable(true);
        createComponents("Edit Categorical Scale", "Edit Categorical Scale", MessageStatus.INFO, null);

    }

    @Override
    protected JComponent createContainer() {
        return page;
    }


    @NotNull
    @Override
    protected List<JButton> createButtons() {
        List<JButton> buttons = new ArrayList<JButton>();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doClose(RET_OK);
            }
        });
        buttons.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doClose(RET_OK);
            }
        });
        buttons.add(cancelButton);

        return buttons;
    }

    public ColoredLabelsGroupsPage getPage() {
        return page;
    }

}

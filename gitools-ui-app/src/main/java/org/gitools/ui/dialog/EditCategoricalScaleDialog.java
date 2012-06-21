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

import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.ui.heatmap.header.coloredlabels.ColoredLabelsGroupsPage;
import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.MessageStatus;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditCategoricalScaleDialog extends AbstractDialog {

	private static final long serialVersionUID = -829366063374164258L;

    protected ColoredLabelsGroupsPage page;

	public EditCategoricalScaleDialog(Window owner, ColoredLabel[] coloredLabels) {
		super(
				owner,
				"Edit Categorical Scale",
				"Edit Categorical Scale",
				"Edit Categorical Scale",
				MessageStatus.INFO,
				null);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setMinimumSize(new Dimension(500, 500));

        this.page = new ColoredLabelsGroupsPage(coloredLabels);
        setContainer(this.page);
        page.setValueEditable(true);
        createComponents("Edit Categorical Scale",
                "Edit Categorical Scale",
                MessageStatus.INFO,
                null);

	}
	
	@Override
	protected JComponent createContainer() {
        return page;
	}


	
	@Override
	protected List<JButton> createButtons() {
		List<JButton> buttons= new ArrayList<JButton>();

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

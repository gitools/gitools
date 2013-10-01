/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * @noinspection ALL
 */
@Deprecated
public abstract class BaseDialog extends JDialog {

    private static final long serialVersionUID = 183914906195441688L;

    private String dialogTitle;
    private String dialogSubtitle;

    public BaseDialog(JFrame owner, String windowTitle, String dialogTitle, String dialogSubtitle) {
        setModal(true);
        setLocationByPlatform(true);
        setTitle(windowTitle);
        setDialogTitle(dialogTitle);
        setDialogSubtitle(dialogSubtitle);
    }

    //Titles
    void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    void setDialogSubtitle(String dialogSubtitle) {
        this.dialogSubtitle = dialogSubtitle;
    }

    abstract void setIcon();

    //Components
    abstract void addButton(JButton button);

    abstract void removeButton(JButton button);

    protected void createComponents(JComponent cntComponent) {

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextArea titleField = new JTextArea();
        titleField.setText(dialogTitle);
        Font titleFont = new Font(titleField.getFont().getName(), titleField.getFont().getStyle(), 24);
        titleField.setFont(titleFont);
        titleField.setEditable(false);
        titleField.setOpaque(false);
        titlePanel.add(titleField, BorderLayout.CENTER);
        if (dialogSubtitle != null) {
            JTextArea subtitleField = new JTextArea();
            subtitleField.setText(dialogSubtitle);
            Font subtitleFont = new Font(subtitleField.getFont().getName(), subtitleField.getFont().getStyle(), 14);
            subtitleField.setFont(subtitleFont);
            subtitleField.setEditable(false);
            subtitleField.setOpaque(false);
            titlePanel.add(subtitleField, BorderLayout.SOUTH);
        }

        this.setMinimumSize(new Dimension(500, 500));
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(cntComponent, BorderLayout.CENTER);
    }

}

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
package org.gitools.ui.platform.settings;

import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;


public class SettingsDialog extends AbstractDialog {

    private final ISettingsPanel panel;

    private boolean cancelled;

    private JButton closeButton;

    private JPanel pagePanel;

    public SettingsDialog(Window owner, ISettingsPanel panel) {
        super(owner, panel.getTitle(), panel.getLogo());

        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(owner);

        this.panel = panel;

        cancelled = true;

        JComponent contents = panel.createComponents();
        pagePanel.add(contents, BorderLayout.CENTER);
        contents.repaint();

        updateState();

    }

    @Override
    protected JComponent createContainer() {
        pagePanel = new JPanel(new BorderLayout());
        return pagePanel;
    }

    protected void close() {
        setVisible(false);
    }

    @Override
    protected List<JButton> createButtons() {

        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        closeButton.setDefaultCapable(true);

        return Arrays.asList(closeButton);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    private void updateState() {
        DialogHeaderPanel header = getHeaderPanel();
        header.setTitle(panel.getTitle());
        header.setLeftLogo(panel.getLogo());
        header.setMessageStatus(panel.getStatus());
        header.setMessage(panel.getMessage());
    }

}

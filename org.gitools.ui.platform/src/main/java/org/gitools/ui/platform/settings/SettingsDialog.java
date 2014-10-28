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
import org.gitools.ui.platform.dialog.DialogButtonsPanel;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;


public abstract class SettingsDialog extends AbstractDialog {

    private final SettingsPanel panel;

    private JButton closeButton;

    private JButton applyButton;

    private JPanel selectedPanel;

    private JPanel sectionPanel;

    public SettingsDialog(Window owner, SettingsPanel panel, String selectedSection) {
        super(owner, panel.getTitle(), panel.getLogo(), new Dimension(700, 500), new Dimension(700, 500));

        this.panel = panel;

        // Sections list
        final JList<String> list = new JList<>(panel.getSectionNames());
        list.setSelectedValue(selectedSection, true);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                showSection(list.getSelectedValue());
            }
        });

        sectionPanel.add(list);

        // Selected panel
        for (String section : panel.getSectionNames()) {
            JComponent components = panel.createComponents(section);
            components.setPreferredSize(new Dimension(450, -1));
            components.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            JScrollPane scrollPane = new JScrollPane(components);
            selectedPanel.add(scrollPane, section);
        }

        // Show current section
        showSection(selectedSection);

        updateState();

    }

    private void showSection(String sectionName) {
        CardLayout layout = (CardLayout) selectedPanel.getLayout();
        layout.show(selectedPanel, sectionName);
        //revalidate();
        //repaint();
    }

    @Override
    protected JComponent createContainer() {
        selectedPanel = new JPanel(new CardLayout());
        selectedPanel.setPreferredSize(new Dimension(400, 300));
        sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setMinimumSize(new Dimension(200, 300));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sectionPanel, selectedPanel);
        splitPane.setMinimumSize(new Dimension(600, 300));
        splitPane.setDividerLocation(0.25);
        return splitPane;
    }

    protected void close() {
        apply();
        setVisible(false);
    }

    @Override
    public void escapePressed() {
        close();
    }

    protected abstract void apply();

    public DialogButtonsPanel getButtonsPanel() {
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
            }
        });

        closeButton.setDefaultCapable(true);
        return new DialogButtonsPanel(Arrays.asList(applyButton, closeButton));
    }

    private void updateState() {
        DialogHeaderPanel header = getHeaderPanel();
        header.setTitle(panel.getTitle());
        header.setLeftLogo(panel.getLogo());
        header.setMessageStatus(panel.getStatus());
        header.setMessage(panel.getMessage());
    }


    @Override
    public void helpActionPerformed() {

    }

    @Override
    public void cancelActionPerformed() {
        setVisible(false);
    }

    @Override
    public void finishActionPerformed() {

    }

}

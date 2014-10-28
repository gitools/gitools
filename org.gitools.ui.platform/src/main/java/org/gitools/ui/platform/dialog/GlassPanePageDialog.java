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

import org.gitools.ui.platform.progress.GitoolsGlassPane;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.platform.wizard.IWizardPageUpdateListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class GlassPanePageDialog extends GitoolsGlassPane implements IDialog {

    private AbstractWizardPage page;
    private int returnStatus = RET_CANCEL;
    private JButton helpButton;
    private JButton cancelButton;
    private JButton finishButton;
    private DialogHeaderPanel headerPanel;
    private boolean cancelled;


    public GlassPanePageDialog(Window parent, AbstractWizardPage page) {
        super(parent);
        setShield(1f);

        if (parent instanceof RootPaneContainer) {
            ((RootPaneContainer) parent).setGlassPane(this);
        }

        createComponents(page);

        page.addPageUpdateListener(new IWizardPageUpdateListener() {
            @Override
            public void pageUpdated(IWizardPage page) {
                updateState();
            }
        });

        this.page = page;

    }



    private void updateState() {
        updateHeaderPanel(page);
        updateButtons();

    }

    protected void createComponents(IWizardPage page) {

        headerPanel = new DialogHeaderPanel();
        updateHeaderPanel(page);

        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BorderLayout());
        headerContainer.add(headerPanel, BorderLayout.CENTER);
        headerContainer.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        JComponent pageContents = page.createControls();

        final DialogButtonsPanel buttonsPanel = getButtonsPanel();

        JPanel bp = new JPanel();
        bp.setLayout(new BorderLayout());
        bp.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        bp.add(buttonsPanel, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(headerContainer, BorderLayout.NORTH);
        if (pageContents != null) {
            this.add(pageContents, BorderLayout.CENTER);
            pageContents.repaint();
            page.updateControls();
        }
        this.add(bp, BorderLayout.SOUTH);
    }

    private void updateHeaderPanel(IWizardPage page) {
        headerPanel.setTitle(page.getTitle());
        headerPanel.setMessage(page.getTitle());
        headerPanel.setMessageStatus(page.getStatus());
        headerPanel.setRightLogo(page.getLogo());
    }

    private void updateButtons() {
        finishButton.setEnabled(page.isComplete());
        cancelButton.setEnabled(true);
    }


    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose();
    }

    private void doClose() {
        setVisible(false);
    }


    public Container getContentPane() {
        return this;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public void escapePressed() {
        cancelActionPerformed();
    }

    @Override
    public void open() {
        this.setVisible(true);
    }

    @Override
    public DialogHeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    @Override
    public JComponent getContainer() {
        return this;
    }

    public DialogButtonsPanel getButtonsPanel() {
        helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpActionPerformed();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelActionPerformed();
            }
        });

        finishButton = new JButton("Ok");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finishActionPerformed();
            }
        });

        finishButton.setDefaultCapable(true);

        return new DialogButtonsPanel(Arrays.asList(cancelButton, finishButton, helpButton));
    }

    public void finishActionPerformed() {
        page.updateModel();
        cancelled = false;
        setVisible(false);
    }

    public void cancelActionPerformed() {
        cancelled = true;
        setVisible(false);
    }

    @Override
    public int getReturnStatus() {
        return returnStatus;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void helpActionPerformed() {

    }
}

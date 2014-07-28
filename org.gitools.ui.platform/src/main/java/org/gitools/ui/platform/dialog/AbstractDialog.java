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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public abstract class AbstractDialog extends JDialog implements IDialog {

    private static final long serialVersionUID = 5886096207448862426L;

    private DialogHeaderPanel hdrPanel;

    private JComponent container;

    private int returnStatus = RET_CANCEL;

    protected AbstractDialog(Window owner, String title, String header, String message, MessageStatus status, Icon logo, Dimension minimum, Dimension prefered) {
        super(owner, title);
        setModal(true);

        createComponents(header, message, status, logo);

        setMinimumSize(minimum);
        setPreferredSize(prefered);
        setLocationRelativeTo(owner);


    }

    protected JRootPane createRootPane() {

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                escapePressed();
            }
        };

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }

    protected AbstractDialog(Window owner, String title, Icon icon, Dimension minimum, Dimension prefered) {
        this(owner, title, "", "", MessageStatus.INFO, icon, minimum, prefered);
    }

    @Override
    public void open() {

        if (!SwingUtilities.isEventDispatchThread()) {
            System.out.println("WARNING: Opening a dialog NOT in the event dispatch thread" );
        }

        super.setVisible(true);
    }

    public void setVisible(boolean b) {

        if (b) {
            if (!SwingUtilities.isEventDispatchThread()) {
                System.out.println("WARNING: Opening a dialog NOT in the event dispatch thread" );
            }
        }

        super.setVisible(b);
    }

    public JComponent getContainer() {
        return container;
    }

    protected void setContainer(JComponent container) {
        this.container = container;
    }

    protected void createComponents(String header, String message, MessageStatus status, Icon logo) {

        hdrPanel = new DialogHeaderPanel();
        hdrPanel.setTitle(header);
        hdrPanel.setMessage(message);
        hdrPanel.setMessageStatus(status);
        hdrPanel.setRightLogo(logo);

        JPanel hp = new JPanel();
        hp.setLayout(new BorderLayout());
        hp.add(hdrPanel, BorderLayout.CENTER);
        hp.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        container = createContainer();

        final DialogButtonsPanel buttonsPanel = getButtonsPanel();

        JPanel bp = new JPanel();
        bp.setLayout(new BorderLayout());
        bp.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        bp.add(buttonsPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(hp, BorderLayout.NORTH);
        if (container != null) {
            add(container, BorderLayout.CENTER);
        }
        add(bp, BorderLayout.SOUTH);
    }


    protected void escapePressed() {
        doClose(returnStatus);
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public DialogHeaderPanel getHeaderPanel() {
        return hdrPanel;
    }


    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    @Override
    public int getReturnStatus() {
        return returnStatus;
    }

    protected abstract JComponent createContainer();

}

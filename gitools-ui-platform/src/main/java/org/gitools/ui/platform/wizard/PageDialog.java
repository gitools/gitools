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
package org.gitools.ui.platform.wizard;

import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.help.Help;
import org.gitools.ui.platform.help.HelpContext;
import org.gitools.ui.platform.help.HelpException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;


public class PageDialog extends AbstractDialog
{

    private IWizardPage page;

    private boolean cancelled;

    private JButton cancelButton;
    private JButton finishButton;
    private JButton helpButton;

    protected JPanel pagePanel;

    public PageDialog(Window owner, IWizardPage page)
    {
        super(owner, page.getTitle(), page.getLogo());

        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(owner);

        this.page = page;

        cancelled = true;

        page.addPageUpdateListener(new IWizardPageUpdateListener()
        {
            @Override
            public void pageUpdated(IWizardPage page)
            {
                updateState();
            }
        });

        JComponent contents = page.createControls();
        pagePanel.add(contents, BorderLayout.CENTER);
        contents.repaint();

        updateState();

        page.updateControls();
    }

    @Override
    protected JComponent createContainer()
    {
        pagePanel = new JPanel(new BorderLayout());
        return pagePanel;
    }

    @Override
    protected List<JButton> createButtons()
    {
        helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                helpActionPerformed();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cancelActionPerformed();
            }
        });

        finishButton = new JButton("Ok");
        finishButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                finishActionPerformed();
            }
        });

        finishButton.setDefaultCapable(true);

        return Arrays.asList(
                cancelButton,
                finishButton,
                helpButton);
    }

    private void helpActionPerformed()
    {
        HelpContext context = page.getHelpContext();
        if (context != null)
        {
            try
            {
                Help.getDefault().showHelp(context);
            } catch (HelpException ex)
            {
                ExceptionDialog dlg = new ExceptionDialog(this, ex);
                dlg.setVisible(true);
            }
        }
    }

    private void cancelActionPerformed()
    {
        cancelled = true;

        setVisible(false);
    }

    private void finishActionPerformed()
    {
        page.updateModel();

        cancelled = false;

        setVisible(false);
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

    private void updateState()
    {
        DialogHeaderPanel header = getHeaderPanel();
        header.setTitle(page.getTitle());
        header.setLeftLogo(page.getLogo());
        header.setMessageStatus(page.getStatus());
        header.setMessage(page.getMessage());

        updateButtons();
    }

    private void updateButtons()
    {
        finishButton.setEnabled(page.isComplete());
        cancelButton.setEnabled(true);
    }
}

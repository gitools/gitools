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
import org.gitools.ui.platform.dialog.DialogButtonsPanel;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.help.Help;
import org.gitools.ui.platform.help.HelpContext;
import org.gitools.ui.platform.help.HelpException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class WizardDialog extends AbstractDialog {

    private static final long serialVersionUID = 1L;

    private final Map<String, JComponent> pageControlsMap;

    private IWizardPage currentPage;

    private final Stack<IWizardPage> pageHistory;

    private JPanel pagePanel;

    private JButton helpButton;
    private JButton backButton;
    private JButton nextButton;
    private JButton finishButton;
    private JButton cancelButton;

    private boolean cancelled;

    public WizardDialog(Window owner, IWizard wizard) {

        super(owner, wizard.getTitle(), wizard.getLogo());

        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(owner);

        pageControlsMap = new HashMap<String, JComponent>();
        pageHistory = new Stack<IWizardPage>();

        wizard.addWizardUpdateListener(new IWizardUpdateListener() {
            @Override
            public void pageUpdated(IWizardPage page) {
                updateState();
            }

            @Override
            public void wizardUpdated(IWizard wizard) {
                updateState();
            }
        });

        wizard.addPages();

        setCurrentPage(wizard.getStartingPage());

        cancelled = true;
    }


    IWizard getWizard() {
        return currentPage != null ? currentPage.getWizard() : null;
    }

    IWizardPage getCurrentPage() {
        return currentPage;
    }

    final void setCurrentPage(IWizardPage page) {
        setCurrentPage(page, true);
    }

    final void setCurrentPage(IWizardPage page, boolean updateHistory) {
        if (currentPage != null) {
            if (updateHistory) {
                pageHistory.push(currentPage);
            }

            currentPage.updateModel();
            getWizard().pageLeft(currentPage);
        }

        currentPage = page;

        getWizard().setCurrentPage(page);

        JComponent contents = getPageContents(page.getId());
        if (contents == null) {
            contents = new JPanel();
        }

        pagePanel.removeAll();
        pagePanel.add(contents, BorderLayout.CENTER);
        contents.repaint();

        updateState();

        page.updateControls();

        getWizard().pageEntered(page);
    }

    private void updateButtons() {
        final IWizardPage page = getCurrentPage();
        final IWizard wizard = page.getWizard();

        backButton.setEnabled(pageHistory.size() > 0);
        nextButton.setEnabled(page.isComplete() && !wizard.isLastPage(page));
        finishButton.setEnabled(page.getWizard().canFinish());
        cancelButton.setEnabled(true);
    }

    JComponent getPageContents(String id) {
        JComponent contents = pageControlsMap.get(id);
        IWizard wizard = getWizard();
        if (contents == null && wizard != null) {
            IWizardPage page = wizard.getPage(id);
            contents = page.createControls();
            pageControlsMap.put(id, contents);
        }
        return contents;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    protected JComponent createContainer() {
        pagePanel = new JPanel(new BorderLayout());
        return pagePanel;
    }


    @Override
    protected List<JButton> createButtons() {

        helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpActionPerformed();
            }
        });

        backButton = new JButton("< Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backActionPerformed();
            }
        });

        nextButton = new JButton("Next >");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextActionPerformed();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelActionPerformed();
            }
        });

        finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finishActionPerformed();
            }
        });

        nextButton.setDefaultCapable(true);

        return Arrays.asList(backButton, nextButton, DialogButtonsPanel.SEPARATOR, cancelButton, DialogButtonsPanel.SEPARATOR, finishButton, DialogButtonsPanel.SEPARATOR, helpButton);
    }

    private void helpActionPerformed() {
        final IWizard wizard = currentPage.getWizard();
        HelpContext context = currentPage.getHelpContext();
        if (context == null) {
            context = wizard.getHelpContext();
        }

        if (context == null) {
            context = new HelpContext("__default__"); //FIXME
        }

        if (context != null) {
            try {
                Help.get().showHelp(context);
            } catch (HelpException ex) {
                ExceptionDialog dlg = new ExceptionDialog(this, ex);
                dlg.setVisible(true);
            }
        }
    }

    private void backActionPerformed() {
        setCurrentPage(pageHistory.pop(), false);
    }

    private void nextActionPerformed() {
        if (currentPage == null) {
            return;
        }

        IWizard wizard = getWizard();

        setCurrentPage(wizard.getNextPage(currentPage));
    }

    private void finishActionPerformed() {
        if (currentPage != null) {
            currentPage.updateModel();
            currentPage.getWizard().pageLeft(currentPage);
            currentPage.getWizard().performFinish();
        }

        cancelled = false;

        setVisible(false);
    }

    private void cancelActionPerformed() {
        if (currentPage != null) {
            currentPage.getWizard().performCancel();
        }

        cancelled = true;

        setVisible(false);
    }

    void updateState() {
        if (currentPage != null) {
            DialogHeaderPanel header = getHeaderPanel();
            header.setTitle(currentPage.getTitle());
            header.setLeftLogo(currentPage.getLogo());
            header.setMessageStatus(currentPage.getStatus());
            header.setMessage(currentPage.getMessage());

            IWizard wizard = getWizard();
            setTitle(wizard.getTitle());
        }
        updateButtons();
    }

}

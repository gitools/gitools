/*
 *  Copyright 2010 cperez.
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

package org.gitools.ui.platform.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.help.Help;
import org.gitools.ui.platform.help.HelpContext;
import org.gitools.ui.platform.help.HelpException;


public class PageDialog extends AbstractDialog {

	private IWizardPage page;

	private boolean cancelled;

	private JButton cancelButton;
	private JButton finishButton;
	private JButton helpButton;

	protected JPanel pagePanel;

	public PageDialog(Window owner, IWizardPage page) {
		super(owner, page.getTitle(), page.getLogo());

		setMinimumSize(new Dimension(800, 600));
		setPreferredSize(new Dimension(800, 600));
		setLocationRelativeTo(owner);

		this.page = page;
		
		cancelled = true;

		page.addPageUpdateListener(new IWizardPageUpdateListener() {
			@Override public void pageUpdated(IWizardPage page) {
				updateState(); }
		});

		JComponent contents = page.createControls();
		pagePanel.add(contents, BorderLayout.CENTER);
		contents.repaint();

		updateState();

		page.updateControls();
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
			@Override public void actionPerformed(ActionEvent e) {
				helpActionPerformed();
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				cancelActionPerformed();
			}
		});

		finishButton = new JButton("Ok");
		finishButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				finishActionPerformed();
			}
		});

		finishButton.setDefaultCapable(true);
		
		return Arrays.asList(
				cancelButton,
				finishButton,
				helpButton);
	}

	private void helpActionPerformed() {
		HelpContext context = page.getHelpContext();
		if (context != null) {
			try {
				Help.getDefault().showHelp(context);
			} catch (HelpException ex) {
				ExceptionDialog dlg = new ExceptionDialog(this, ex);
				dlg.setVisible(true);
			}
		}
	}

	private void cancelActionPerformed() {
		cancelled = true;

		setVisible(false);
	}
	
	private void finishActionPerformed() {
		page.updateModel();

		cancelled = false;

		setVisible(false);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	private void updateState() {
		DialogHeaderPanel header = getHeaderPanel();
		header.setTitle(page.getTitle());
		header.setLeftLogo(page.getLogo());
		header.setMessageStatus(page.getStatus());
		header.setMessage(page.getMessage());

		updateButtons();
	}

	private void updateButtons() {
		finishButton.setEnabled(page.isComplete());
		cancelButton.setEnabled(true);
	}
}

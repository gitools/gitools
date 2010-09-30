package org.gitools.ui.platform.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.DialogButtonsPanel;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.help.Help;
import org.gitools.ui.platform.help.HelpContext;
import org.gitools.ui.platform.help.HelpException;

public class WizardDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	protected Map<String, JComponent> pageControlsMap;
	
	protected IWizardPage currentPage;
	
	protected Stack<IWizardPage> pageHistory;
	
	protected JPanel pagePanel;

	protected JButton helpButton;
	protected JButton backButton;
	protected JButton nextButton;
	protected JButton finishButton;
	protected JButton cancelButton;

	protected boolean cancelled;

	public WizardDialog(Window owner, IWizard wizard) {
		
		super(owner, wizard.getTitle(), wizard.getLogo());

		setMinimumSize(new Dimension(800, 600));
		setPreferredSize(new Dimension(800, 600));
		setLocationRelativeTo(owner);
		
		pageControlsMap = new HashMap<String, JComponent>();
		pageHistory = new Stack<IWizardPage>();
		
		wizard.setDialog(this);
		wizard.addPages();

		setCurrentPage(wizard.getStartingPage());

		cancelled = true;
	}

	public IWizard getWizard() {
		return currentPage != null ? currentPage.getWizard() : null;
	}
	
	public IWizardPage getCurrentPage() {
		return currentPage;
	}
	
	protected final void setCurrentPage(IWizardPage page) {
		setCurrentPage(page, true);
	}
	
	protected final void setCurrentPage(IWizardPage page, boolean updateHistory) {
		if (currentPage != null) {
			if (updateHistory)
				pageHistory.push(currentPage);
			
			currentPage.updateModel();
		}
		
		currentPage = page;
		
		getWizard().setCurrentPage(page);
		
		JComponent contents = getPageContents(page.getId());
		if (contents == null)
			contents = new JPanel();
		
		pagePanel.removeAll();
		pagePanel.add(contents, BorderLayout.CENTER);
		contents.repaint();
		
		updateState();
		
		page.updateControls();
	}

	private void updateButtons() {
		final IWizardPage page = getCurrentPage();
		final IWizard wizard = page.getWizard();
		
		backButton.setEnabled(pageHistory.size() > 0);
		nextButton.setEnabled(page.isComplete() && !wizard.isLastPage(page));
		finishButton.setEnabled(page.getWizard().canFinish());
		cancelButton.setEnabled(true);
	}
	
	protected JComponent getPageContents(String id) {
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
			@Override public void actionPerformed(ActionEvent e) {
				helpActionPerformed();
			}
		});

		backButton = new JButton("< Back");
		backButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				backActionPerformed();
			}
		});
		
		nextButton = new JButton("Next >");
		nextButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				nextActionPerformed();
			}
		});
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				cancelActionPerformed();
			}
		});
		
		finishButton = new JButton("Finish");
		finishButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				finishActionPerformed();
			}
		});

		nextButton.setDefaultCapable(true);
		
		return Arrays.asList(
				backButton,
				nextButton,
				DialogButtonsPanel.SEPARATOR,
				cancelButton,
				DialogButtonsPanel.SEPARATOR,
				finishButton,
				DialogButtonsPanel.SEPARATOR,
				helpButton);
	}

	private void helpActionPerformed() {
		HelpContext context = currentPage.getHelpContext();
		if (context != null) {
			try {
				Help.getDefault().showHelp(context);
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
		if (currentPage == null)
			return;

		IWizard wizard = getWizard();

		setCurrentPage(wizard.getNextPage(currentPage));
	}

	private void finishActionPerformed() {
		if (currentPage != null) {
			currentPage.updateModel();
			currentPage.getWizard().performFinish();
		}

		cancelled = false;
		
		setVisible(false);
	}
	
	private void cancelActionPerformed() {
		if (currentPage != null)
			currentPage.getWizard().performCancel();

		cancelled = true;
		
		setVisible(false);
	}

	public void updateState() {
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

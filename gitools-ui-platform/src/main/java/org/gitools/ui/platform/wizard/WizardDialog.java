package org.gitools.ui.platform.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class WizardDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	protected Map<String, JComponent> pageControlsMap;
	
	protected IWizardPage currentPage;
	
	protected Stack<IWizardPage> pageHistory;
	
	protected JPanel pagePanel;
	
	protected JButton backButton;
	protected JButton nextButton;
	protected JButton finishButton;
	protected JButton cancelButton;

	protected boolean cancelled;

	public WizardDialog(Window owner, IWizard wizard) {
		
		super(owner, wizard.getTitle(), wizard.getIcon());

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
	
	protected void setCurrentPage(IWizardPage page) {
		setCurrentPage(page, true);
	}
	
	protected void setCurrentPage(IWizardPage page, boolean updateHistory) {
		if (currentPage != null && updateHistory)
			pageHistory.push(currentPage);
		
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
		
		return Arrays.asList(
				backButton,
				nextButton,
				DialogButtonsPanel.SEPARATOR,
				cancelButton,
				DialogButtonsPanel.SEPARATOR,
				finishButton);
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
		if (currentPage != null)
			currentPage.getWizard().performFinish();

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
			header.setMessageStatus(currentPage.getStatus());
			header.setMessage(currentPage.getMessage());
			
			IWizard wizard = getWizard();
			setTitle(wizard.getTitle());
		}
		updateButtons();
	}

}

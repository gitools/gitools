package org.gitools.ui.wizard;

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

import org.gitools.ui.dialog.AbstractDialog;
import org.gitools.ui.dialog.DialogHeaderPanel;

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

	public WizardDialog(Window owner, IWizard wizard) {
		
		super(owner, wizard.getTitle(), wizard.getIcon());
		
		setMinimumSize(new Dimension(800, 600));
		setPreferredSize(new Dimension(800, 600));
		
		pageControlsMap = new HashMap<String, JComponent>();
		pageHistory = new Stack<IWizardPage>();
		
		wizard.setDialog(this);
		wizard.addPages();

		setCurrentPage(wizard.getStartingPage());
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
		
		getWizard().setCurrentPage(currentPage);
		
		JComponent contents = getPageContents(page.getId());
		if (contents == null)
			contents = new JPanel();
		
		pagePanel.removeAll();
		pagePanel.add(contents, BorderLayout.CENTER);
		contents.repaint();
		
		updateState();
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

	@Override
	protected JComponent createContainer() {
		pagePanel = new JPanel(new BorderLayout());
		return pagePanel;
	}
	
	@Override
	protected List<JButton> createButtons() {
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				backActionPerformed();
			}
		});
		
		nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				nextActionPerformed();
			}
		});
		
		finishButton = new JButton("Finish");
		finishButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				finishActionPerformed();
			}
		});
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				cancelActionPerformed();
			}
		});
		
		return Arrays.asList(
				backButton,
				nextButton,
				finishButton,
				cancelButton);
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
		
		setVisible(false);
	}
	
	private void cancelActionPerformed() {
		if (currentPage != null)
			currentPage.getWizard().performCancel();
		
		setVisible(false);
	}

	public void updateState() {
		if (currentPage != null) {
			DialogHeaderPanel header = getHeaderPanel();
			header.setHeader(currentPage.getTitle());
			
			IWizard wizard = getWizard();
			setTitle(wizard.getTitle());
		}
		updateButtons();
	}

}

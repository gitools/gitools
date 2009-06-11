package org.gitools.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Window;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.gitools.ui.dialog.AbstractDialog;
import org.gitools.ui.dialog.DialogHeaderPanel.Status;

public abstract class WizardDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	protected List<IWizardPage> pages;
	
	protected Map<Object, IWizardPage> pageIdMap;
	protected Map<Object, JComponent> pageControlsMap;
	
	protected IWizardPage currentPage;
	
	protected Stack<IWizardPage> pageHistory;
	
	protected JPanel pagePanel;
	
	protected JButton backButton;
	protected JButton nextButton;
	protected JButton finishButton;
	protected JButton cancelButton;

	public WizardDialog(
			Window owner, String title, String header,
			String message, Status status, Icon logo) {
		
		super(owner, title, header, message, status, logo);
		
		pages = createPages();
		pageIdMap = new HashMap<Object, IWizardPage>();
		pageControlsMap = new HashMap<Object, JComponent>();
		pageHistory = new Stack<IWizardPage>();
		
		for (IWizardPage page : pages) {
			pageIdMap.put(page.getId(), page);
			page.setWizard(this);
		}
		
		setCurrentPage(pages.get(0));
	}

	public IWizardPage getCurrentPage() {
		return currentPage;
	}
	
	protected void setCurrentPage(IWizardPage page) {
		JComponent contents = getPageContents(page.getId());
		pagePanel.removeAll();
		pagePanel.add(contents, BorderLayout.CENTER);
		
		pageHistory.push(currentPage);
		currentPage = page;
		
		updateButtons();
	}

	public void addPage(WizardPage page) {
		pages.add(page);
	}
	
	private void updateButtons() {
		final IWizardPage page = getCurrentPage();
		
		backButton.setEnabled(pageHistory.size() > 0);
		nextButton.setEnabled(page.isPageComplete() && page.canFlipToNextPage());
		finishButton.setEnabled(canFinish());
		cancelButton.setEnabled(true);
	}
	
	protected JComponent getPageContents(Object id) {
		JComponent contents = pageControlsMap.get(id);
		if (contents == null) {
			IWizardPage page = pageIdMap.get(id);
			contents = page.createPageControls();
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
		nextButton = new JButton("Next");
		finishButton = new JButton("Finish");
		cancelButton = new JButton("Cancel");
		
		return Arrays.asList(
				backButton,
				nextButton,
				finishButton,
				cancelButton);
	}
	
	protected abstract List<IWizardPage> createPages();
	
	protected abstract boolean canFinish();

}

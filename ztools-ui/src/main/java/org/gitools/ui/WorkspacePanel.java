package org.gitools.ui;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.ui.actions.Actions;
import org.gitools.ui.actions.FileActionSet;
import org.gitools.ui.actions.HelpActionSet;
import org.gitools.ui.actions.MenuActionSet;
import org.gitools.ui.editor.AbstractEditor;


public class WorkspacePanel extends JTabbedPane {

	private static final long serialVersionUID = 2170150185478413716L;

	private AbstractEditor selectedView;
	
	//private JTabbedPane tabbedPane;
	
	public WorkspacePanel() {
		this.selectedView = null;
	
		createComponents();
		
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				/*if (selectedView != null)
					selectedView.disableActions();*/
				refreshActions();
				
				selectedView = (AbstractEditor) getSelectedComponent();
				if (selectedView != null)
					selectedView.refreshActions();
			}
		});
	}
	
	private void createComponents() {
		//tabbedPane = new JTabbedPane();
	}
	
	public void addView(AbstractEditor view) {
		if (view == null)
			return;
		
		final String name = view.getName() != null ? 
				view.getName() : "";
				
		final Icon icon = view.getIcon();
		
		if (icon == null)
			addTab(name, view);
		else
			addTab(name, icon, view);
		
		/*if (selectedView != null)
			selectedView.disableActions();*/
		refreshActions();
		
		setSelectedComponent(view);
		
		selectedView = view;
		selectedView.refreshActions();
	}

	public void removeView(AbstractEditor view) {
		if (view == null)
			return;
		
		int i = indexOfComponent(view);
		if (i != -1)
			remove(i);
		
		refreshActions();
		
		view = (AbstractEditor) getSelectedComponent();
		if (view != null)
			view.refreshActions();
	}
	
	public AbstractEditor getSelectedView() {
		return (AbstractEditor) getSelectedComponent();
	}
	
	private void refreshActions() {
		Actions.menuActionSet.setTreeEnabled(false);
		
		MenuActionSet.fileActionSet.setEnabled(true);
		MenuActionSet.helpActionSet.setEnabled(true);
		
		FileActionSet.newZCalcAnalysisAction.setEnabled(true);
		FileActionSet.openAnalysisAction.setEnabled(true);
		FileActionSet.closeAction.setEnabled(getTabCount() > 0);
		FileActionSet.exportWizardAction.setEnabled(true);
		FileActionSet.exitAction.setEnabled(true);

		HelpActionSet.welcomeAction.setEnabled(true);
		HelpActionSet.aboutAction.setEnabled(true);
	}
}

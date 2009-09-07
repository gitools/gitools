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

	private AbstractEditor selectedEditor;
	
	//private JTabbedPane tabbedPane;
	
	public WorkspacePanel() {
		this.selectedEditor = null;
	
		createComponents();
		
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				/*if (selectedView != null)
					selectedView.disableActions();*/
				refreshActions();
				
				selectedEditor = (AbstractEditor) getSelectedComponent();
				if (selectedEditor != null)
					selectedEditor.refreshActions();
			}
		});
	}
	
	private void createComponents() {
		//tabbedPane = new JTabbedPane();
	}
	
	public void addEditor(AbstractEditor editor) {
		if (editor == null)
			return;
		
		final String name = editor.getName() != null ? 
				editor.getName() : "";
				
		final Icon icon = editor.getIcon();
		
		if (icon == null)
			addTab(name, editor);
		else
			addTab(name, icon, editor);
		
		/*if (selectedView != null)
			selectedView.disableActions();*/
		refreshActions();
		
		setSelectedComponent(editor);
		
		selectedEditor = editor;
		selectedEditor.refreshActions();
	}

	public void removeEditor(AbstractEditor editor) {
		if (editor == null)
			return;
		
		int i = indexOfComponent(editor);
		if (i != -1)
			remove(i);
		
		refreshActions();
		
		editor = (AbstractEditor) getSelectedComponent();
		if (editor != null)
			editor.refreshActions();
	}
	
	public AbstractEditor getSelectedEditor() {
		return (AbstractEditor) getSelectedComponent();
	}
	
	private void refreshActions() {
		Actions.menuActionSet.setTreeEnabled(false);
		
		AbstractEditor editor = getSelectedEditor();
		if (editor != null) {
			Object model = editor.getModel();
			if (model != null)
				Actions.menuActionSet.updateEnabledForModel(model);
		}
		
		MenuActionSet.fileActionSet.setEnabled(true);
		MenuActionSet.helpActionSet.setEnabled(true);
		
		FileActionSet.newActionSet.setTreeEnabled(true);
		FileActionSet.openActionSet.setTreeEnabled(true);
		FileActionSet.closeAction.setEnabled(getTabCount() > 0);
		FileActionSet.importActionSet.setTreeEnabled(true);
		FileActionSet.exportWizardAction.setEnabled(true);
		FileActionSet.exitAction.setEnabled(true);

		HelpActionSet.welcomeAction.setEnabled(true);
		HelpActionSet.aboutAction.setEnabled(true);
	}
}

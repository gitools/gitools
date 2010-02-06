package org.gitools.ui.platform;


import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.ui.platform.actions.ActionManager;
import org.gitools.ui.platform.component.TabCloseButton;
import org.gitools.ui.platform.editor.AbstractEditor;

public class EditorsPanel extends JTabbedPane {

	private static final long serialVersionUID = 2170150185478413716L;
	
	//private JTabbedPane tabbedPane;
	
	public EditorsPanel() {	
		createComponents();
		
		addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent evt) {
				refreshActions();

				AbstractEditor selectedEditor = getSelectedEditor();
				if (selectedEditor != null)
					selectedEditor.doVisible();
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

		setTabComponentAt(getTabCount() - 1, new TabCloseButton(this));
		
		refreshActions();
		
		setSelectedComponent(editor);
	}

	public void removeEditor(AbstractEditor editor) {
		if (editor == null)
			return;
		
		int i = indexOfComponent(editor);
		if (i != -1)
			remove(i);
		
		refreshActions();
	}
	
	public AbstractEditor getSelectedEditor() {
		return (AbstractEditor) getSelectedComponent();
	}
	
	public void refreshActions() {
		AbstractEditor editor = getSelectedEditor();
		ActionManager.getDefault().updateEnabledByEditor(editor);
	}
}

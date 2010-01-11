package org.gitools.ui.platform;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.ui.actions.ActionSet;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.component.TabCloseButton;
import org.gitools.ui.platform.editor.AbstractEditor;

public class EditorsPanel extends JTabbedPane {

	private static final long serialVersionUID = 2170150185478413716L;

	private static final Set<BaseAction> actions = new HashSet<BaseAction>();
	static {
		Stack<BaseAction> actionStack = new Stack<BaseAction>();
		actionStack.push(Actions.menuActionSet);
		actionStack.push(Actions.toolBarActionSet);
		while (actionStack.size() > 0) {
			BaseAction action = actionStack.pop();
			actions.add(action);
			if (action instanceof ActionSet) {
				ActionSet as = (ActionSet) action;
				for (BaseAction a : as.getActions())
					actionStack.push(a);
			}
		}
	}

	//private AbstractEditor selectedEditor;
	
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
		
		/*if (selectedView != null)
			selectedView.disableActions();*/
		refreshActions();
		
		setSelectedComponent(editor);
		
		//selectedEditor = editor;
		//selectedEditor.refreshActions();
	}

	public void removeEditor(AbstractEditor editor) {
		if (editor == null)
			return;
		
		int i = indexOfComponent(editor);
		if (i != -1)
			remove(i);
		
		refreshActions();
		
		/*editor = (AbstractEditor) getSelectedComponent();
		if (editor != null)
			editor.refreshActions();*/
	}
	
	public AbstractEditor getSelectedEditor() {
		return (AbstractEditor) getSelectedComponent();
	}
	
	public void refreshActions() {
		AbstractEditor editor = getSelectedEditor();
		Actions.menuActionSet.updateEnabledByEditor(editor);
		Actions.toolBarActionSet.updateEnabledByEditor(editor);
	}
}

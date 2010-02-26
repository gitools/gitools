package org.gitools.ui.platform.editor;


import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.ui.platform.actions.ActionManager;
import org.gitools.ui.platform.component.EditorTabComponent;

public class EditorsPanel extends JTabbedPane {

	private static final long serialVersionUID = 2170150185478413716L;

	public static final String DEFAULT_NAME_PREFIX = "unnamed-";

	private int nameCount = 0;

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
				editor.getName() : createName();
				
		final Icon icon = editor.getIcon();
		
		if (icon == null)
			addTab(name, editor);
		else
			addTab(name, icon, editor);

		setTabComponentAt(getTabCount() - 1, new EditorTabComponent(this, editor));
		
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

	public String createName() {
		return createName(DEFAULT_NAME_PREFIX, "");
	}

	public String createName(String prefix, String suffix) {
		Set<String> names = new HashSet<String>();
		int numTabs = getTabCount();
		for (int i = 0; i < numTabs; i++) {
			IEditor editor = (IEditor) getComponentAt(i);
			names.add(editor.getName());
		}

		String name = prefix + (nameCount++) + suffix;
		while (names.contains(name))
			name = prefix + (nameCount++) + suffix;
		
		return name;
	}
}

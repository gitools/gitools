/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.ui.platform.editor;


import org.gitools.ui.platform.actions.ActionManager;
import org.gitools.ui.platform.component.EditorTabComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EditorsPanel extends JTabbedPane {

	private static final long serialVersionUID = 2170150185478413716L;

	public static final String DEFAULT_NAME_PREFIX = "unnamed";

	private Map<String, Integer> nameCounts = new HashMap<String, Integer>();

	//private AbstractEditor.EditorListener editorListener;

	//private JTabbedPane tabbedPane;
	
	public EditorsPanel() {
		createComponents();

		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent evt) {
				AbstractEditor selectedEditor = getSelectedEditor();
				if (selectedEditor != null)
					selectedEditor.doVisible();

				refreshActions();
			}
		});

		/*editorListener = new AbstractEditor.EditorListener() {
			@Override public void dirtyChanged(IEditor editor) {
				EditorTabComponent tab = getEditorTab(editor);
				
			}
		};*/
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

		//editor.addEditorListener(editorListener);
		
		refreshActions();
		
		setSelectedComponent(editor);
	}

	public void removeEditor(AbstractEditor editor) {
		if (editor == null)
			return;

		//editor.removeEditorListener(editorListener);

		if (editor.doClose()) {
			int i = indexOfComponent(editor);
			if (i != -1)
				remove(i);

			refreshActions();
		}
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

		prefix = prefix.replace(" ", "_");
		Integer c = nameCounts.get(prefix);
		if (c == null)
			c = 1;

		int nameCount = c;
		String name = prefix + "-" + (nameCount++) + suffix;
		while (names.contains(name))
			name = prefix + "-" + (nameCount++) + suffix;

		nameCounts.put(prefix, nameCount);
		return name;
	}

	public String deriveName(String name, String removeExtension, String prefixAdd, String newExtension) {
		if (!removeExtension.isEmpty() && name.endsWith(removeExtension)) {
			int endIndex = name.length() - removeExtension.length() - 1;
			name = endIndex >= 0 ? name.substring(0, endIndex) : "";
		}
		
		int i = name.length() - 1;
		while (i >= 0 && Character.isDigit(name.charAt(i))) i--;
		if (name.charAt(i) != '-')
			i++;

		name = name.substring(0, i);

		if (!name.endsWith(prefixAdd))
			name += prefixAdd;

        if (!newExtension.equals(""))
            newExtension = "." + newExtension;
		
		return createName(name, newExtension);
	}

	private EditorTabComponent getEditorTab(IEditor editor) {
		int index = getEditorIndex(editor);
		return (EditorTabComponent) getTabComponentAt(index);
	}

	private int getEditorIndex(IEditor editor) {
		for (int i = 0; i < getComponentCount(); i++)
			if (getComponent(i) == editor)
				return i;
		return -1;
	}

	public void setSelectedEditor(AbstractEditor editor) {
		setSelectedComponent(editor);
	}
}

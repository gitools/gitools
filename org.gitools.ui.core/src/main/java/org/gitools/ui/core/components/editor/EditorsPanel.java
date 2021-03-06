/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.core.components.editor;

import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import org.gitools.api.components.IEditor;
import org.gitools.ui.core.actions.ActionManager;
import org.gitools.ui.platform.component.scrollbar.Gray;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EditorsPanel extends WebTabbedPane {

    private static final String DEFAULT_NAME_PREFIX = "unnamed";


    private final Map<String, Integer> nameCounts = new HashMap<>();

    public EditorsPanel() {

        setTabbedPaneStyle(TabbedPaneStyle.attached);

        setBackground(Gray._248);

        createComponents();

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {

                AbstractEditor selectedEditor = getSelectedEditor();
                if (selectedEditor != null) {
                    selectedEditor.doVisible();

                    for (int i = 0; i < getTabCount(); i++) {
                        Component component = getTabComponentAt(i);
                        if (component instanceof EditorTabComponent) {
                            AbstractEditor editor = ((EditorTabComponent) component).getEditor();

                            if (editor != selectedEditor) {
                                editor.detach();
                            }
                        }
                    }
                }

                refreshActions();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        System.gc();
                    }
                });
            }
        });
    }

    public AbstractEditor[] getEditors() {
        AbstractEditor[] editors = new AbstractEditor[getTabCount()];
        for (int i = 0; i < editors.length; i++) {
            Component component = getTabComponentAt(i);
            if (component instanceof EditorTabComponent) {
                editors[i] = ((EditorTabComponent) component).getEditor();
            }
        }
        return editors;
    }

    private void createComponents() {

    }

    public void addEditor(AbstractEditor editor) {
        if (editor == null) {
            return;
        }

        final String name = editor.getName() != null ? editor.getName() : createName();

        final Icon icon = editor.getIcon();

        if (icon == null) {
            addTab(name, editor);
        } else {
            addTab(name, icon, editor);
        }

        int index = getTabCount() - 1;
        setTabComponentAt(index, new EditorTabComponent(this, editor));
        setBackgroundAt(index, Color.WHITE);

        refreshActions();

        setSelectedComponent(editor);
    }

    public void removeEditor(AbstractEditor editor) {
        if (editor == null) {
            return;
        }

        if (editor.doClose()) {
            removeEditorNoUI(editor);
        }
    }

    public void removeEditorNoUI(AbstractEditor editor) {
        int i = indexOfComponent(editor);
        if (i != -1) {
            remove(i);
        }

        refreshActions();
    }


    public AbstractEditor getSelectedEditor() {
        return (AbstractEditor) getSelectedComponent();
    }

    void refreshActions() {
        AbstractEditor editor = getSelectedEditor();
        ActionManager.getDefault().updateEnabledByEditor(editor);
    }


    String createName() {
        return createName(DEFAULT_NAME_PREFIX, "");
    }


    String createName(String prefix, String suffix) {
        Set<String> names = new HashSet<>();
        int numTabs = getTabCount();
        for (int i = 0; i < numTabs; i++) {
            IEditor editor = (IEditor) getComponentAt(i);
            names.add(editor.getName());
        }

        prefix = prefix.replace(" ", "_");
        Integer c = nameCounts.get(prefix);
        if (c == null) {
            c = 1;
        }

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
        while (i >= 0 && Character.isDigit(name.charAt(i)))
            i--;
        if (name.charAt(i) != '-') {
            i++;
        }

        name = name.substring(0, i);

        if (!name.endsWith(prefixAdd)) {
            name += prefixAdd;
        }

        if (!newExtension.equals("")) {
            newExtension = "." + newExtension;
        }

        return createName(name, newExtension);
    }

    public void setSelectedEditor(AbstractEditor editor) {
        setSelectedComponent(editor);
    }
}

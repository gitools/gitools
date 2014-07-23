/*
 * #%L
 * org.gitools.heatmap
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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


import org.gitools.api.components.IEditor;
import org.gitools.api.components.IEditorCreator;
import org.gitools.api.components.IEditorManager;
import org.gitools.ui.core.Application;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EditorManager implements IEditorManager {


    @Inject
    @Any
    private Instance<IEditorCreator> iterator;
    private List<IEditorCreator> creators;

    public EditorManager() {
    }

    @PostConstruct
    public void init() {
        creators = new ArrayList<>();
        for (IEditorCreator editor : iterator) {
            creators.add(editor);
        }
    }

    @Override
    public IEditor createEditor(Object object) {
        for (IEditorCreator editor : iterator) {
            if(editor.canCreate(object)) {
                return editor.create(object);
            }
        }
        return null;
    }

    public void addEditor(Object object) {
        IEditor editor = createEditor(object);
        if (editor != null) {
            Application.get().addEditor(editor);
        }
    }

}

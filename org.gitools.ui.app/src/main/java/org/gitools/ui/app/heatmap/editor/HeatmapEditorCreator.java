/*
 * #%L
 * org.gitools.ui.app
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
package org.gitools.ui.app.heatmap.editor;

import org.gitools.api.components.IEditor;
import org.gitools.api.components.IEditorCreator;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.impl.BinaryDecorator;
import org.gitools.matrix.modulemap.ModuleMapUtils;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HeatmapEditorCreator implements IEditorCreator {

    @Override
    public boolean canCreate(Object object) {
        return (object instanceof Heatmap) || (IModuleMap.class.isAssignableFrom(object.getClass()));
    }

    @Override
    public IEditor create(Object object) {
        if (object instanceof Heatmap) {
            return new HeatmapEditor((Heatmap) object);

        } else if (IModuleMap.class.isAssignableFrom(object.getClass())) {
            Heatmap heatmap = new Heatmap(ModuleMapUtils.convertToMatrix((IModuleMap) object));
            heatmap.getLayers().iterator().next().setDecorator(new BinaryDecorator());
            return new HeatmapEditor(heatmap);
        }

        return null;
    }
}

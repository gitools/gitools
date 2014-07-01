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

package org.gitools.ui.app.heatmap.panel.details.boxes;

import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.boxes.Box;
import org.gitools.ui.core.components.editor.AbstractEditor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BoxManager {

    private static HashSet<String> protect = new HashSet<>();


    public static void focusBox(String... boxIds) {
        Collection<Box> boxes = getBoxes();
        for (Box b : boxes) {
            for (String requiredId : boxIds) {
                if (b.isVisible() && !protect.contains(b.getId()) && b.getId().equals(requiredId)) {
                    uncollapse(b);
                } else if (b.isVisible() && !protect.contains(b.getId()) && !b.getId().equals(requiredId)) {
                    collapse(b);
                }
            }
        }
    }

    public static Collection<Box> getBoxes() {
        Set<Box> boxes = new HashSet();
        AbstractEditor editor = Application.get().getEditorsPanel().getSelectedEditor();
        if (editor instanceof HeatmapEditor) {
            return ((HeatmapEditor) editor).getBoxes();
        }
        return boxes;
    }


    public static void collapse(Box... boxes) {
        for (Box b : boxes) {
            b.setCollapsed(true);
        }
    }

    public static void uncollapse(Box... boxes) {
        for (Box b : boxes) {
            b.setCollapsed(false);
        }
    }

    public static void protect(String... ids) {
        protect = new HashSet<>();
        for (String id : ids) {
            protect.add(id);
        }
    }

    public static void reset() {
        protect.clear();
    }
}

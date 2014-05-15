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
package org.gitools.ui.core.components.boxes;

import com.alee.laf.scroll.WebScrollPane;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.ActionSetUtils;
import org.jdesktop.swingx.JXTaskPane;

import javax.swing.*;
import java.awt.*;

public abstract class Box extends JXTaskPane {

    protected JPopupMenu popupMenu;
    private Heatmap heatmap;

    public Box(String title, ActionSet contextActionSet, Heatmap heatmap) {
        setTitle(title);
        setSpecial(true);

        this.heatmap = heatmap;

        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(new WebScrollPane(getContainer(), false, false));

        if (contextActionSet != null) {
            popupMenu = ActionSetUtils.createPopupMenu(contextActionSet);
        }
    }

    abstract Container getContainer();

    abstract void initContainer();

    public abstract void registerListeners();

    public abstract void update();

    public abstract boolean isVisible();

    public Heatmap getHeatmap() {
        return heatmap;
    }
}

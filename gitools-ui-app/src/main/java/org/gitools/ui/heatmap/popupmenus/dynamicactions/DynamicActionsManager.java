/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.heatmap.popupmenus.dynamicactions;

import org.gitools.core.heatmap.drawer.HeatmapPosition;

import javax.swing.*;

public class DynamicActionsManager {

    public static <T> void updatePopupMenu(JPopupMenu menu, Class<? extends IDynamicAction<T>> actionClass, T object, HeatmapPosition position) {

        for (MenuElement element : menu.getSubElements()) {
            if (element instanceof JMenuItem) {
                Action action = ((JMenuItem) element).getAction();

                if (actionClass.isAssignableFrom(action.getClass())) {
                    IDynamicAction<T> dynamicAction = (IDynamicAction<T>) action;
                    dynamicAction.onConfigure(object, position);
                }

                ((JMenuItem) element).setVisible(action.isEnabled());
            }
        }

    }
}

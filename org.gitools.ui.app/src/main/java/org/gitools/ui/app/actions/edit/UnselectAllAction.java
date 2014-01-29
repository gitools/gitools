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
package org.gitools.ui.app.actions.edit;

import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.platform.Application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class UnselectAllAction extends HeatmapAction {

    private static final long serialVersionUID = 1581417292789818975L;

    public UnselectAllAction() {
        super("Unselect all");

        setSmallIconFromResource(IconNames.unselectAll16);
        setLargeIconFromResource(IconNames.unselectAll24);
        setMnemonic(KeyEvent.VK_U);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        getHeatmap().getColumns().getSelected().clear();
        getHeatmap().getRows().getSelected().clear();

        Application.get().setStatusText("Unselected all.");
    }

}

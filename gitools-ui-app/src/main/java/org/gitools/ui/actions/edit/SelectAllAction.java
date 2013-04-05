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
package org.gitools.ui.actions.edit;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @noinspection ALL
 */
public class SelectAllAction extends BaseAction
{

    private static final long serialVersionUID = 3088237733885396229L;

    public SelectAllAction()
    {
        super("Select all");

        setDesc("Select all");
        setSmallIconFromResource(IconNames.selectAll16);
        setLargeIconFromResource(IconNames.selectAll24);
        setMnemonic(KeyEvent.VK_A);
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        IMatrixView matrixView = ActionUtils.getMatrixView();

        if (matrixView != null)
        {
            matrixView.selectAll();
        }

        AppFrame.get().setStatusText("Selected all.");
    }

}

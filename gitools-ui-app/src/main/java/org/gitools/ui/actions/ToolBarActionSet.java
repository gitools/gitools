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
package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.*;

/**
 * @noinspection ALL
 */
public final class ToolBarActionSet extends ActionSet
{

    private static final long serialVersionUID = 6924230823891805344L;

    public ToolBarActionSet()
    {
        super(new BaseAction[]{FileActions.openAction, FileActions.saveAction, BaseAction.separator, FileActions.openGenomeSpaceAction, FileActions.openIntegrativeGenomicViewerAction, HeatmapActions.cloneAction});
    }

    public JToolBar createToolBar()
    {
        return ActionSetUtils.createToolBar(this);
    }
}

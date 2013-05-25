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

import org.gitools.ui.actions.edit.*;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.utils.HeaderEnum.Dimension;

public final class EditActions {

    public static final BaseAction selectAllAction = new SelectAllAction();

    public static final BaseAction selectLabelHeaderAction = new SelectLabelHeaderAction();

    public static final BaseAction invertSelectionAction = new InvertSelectionAction(Dimension.NONE_SPECIFIED);

    public static final BaseAction invertRowSelectionAction = new InvertSelectionAction(Dimension.ROW);

    public static final BaseAction invertColumnSelectionAction = new InvertSelectionAction(Dimension.COLUMN);

    public static final BaseAction unselectAllAction = new UnselectAllAction();

    public static final BaseAction addRowHeader = new AddHeaderAction(Dimension.ROW);

    public static final BaseAction addColumnHeader = new AddHeaderAction(Dimension.COLUMN);

    public static final BaseAction editRowHeader = new EditHeaderAction(Dimension.ROW);

    public static final BaseAction editColumnHeader = new EditHeaderAction(Dimension.COLUMN);

}

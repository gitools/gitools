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

import org.gitools.ui.actions.data.*;
import org.gitools.ui.actions.data.HideSelectionAction.ElementType;
import org.gitools.ui.actions.data.MoveSelectionAction.MoveDirection;
import org.gitools.ui.utils.HeaderEnum.Dimension;
import org.gitools.ui.platform.actions.BaseAction;

public final class DataActions {

    public static final BaseAction copyToClipboardSelectedLabelHeader = new CopyToClipboardSelectedLabelHeaderAction();

    public static final BaseAction filterByLabelAction = new FilterByAnnotations();

    public static final BaseAction filterByValueAction = new FilterByValueAction();

    public static final BaseAction showAllRowsAction = new ShowAllAction(ShowAllAction.ElementType.ROWS);

    public static final BaseAction showAllColumnsAction = new ShowAllAction(ShowAllAction.ElementType.COLUMNS);

    public static final BaseAction hideSelectedColumnsAction = new HideSelectionAction(ElementType.COLUMNS);

    public static final BaseAction hideThisLabelHeaderAction = new HideThisLabelHeaderAction();

    public static final BaseAction hideGreaterThanHeaderAction = new HideNumericHeaderAction(true, "greater");

    public static final BaseAction hideSmallerThanHeaderAction = new HideNumericHeaderAction(false, "smaller");

    public static final BaseAction fastSortRowsAction = new FastSortRowsAction();

    public static final BaseAction sortByAnnotationAction = new SortByAnnotationAction(Dimension.NONE_SPECIFIED);

    public static final BaseAction sortByRowAnnotationAction = new SortByAnnotationAction(Dimension.ROW);

    public static final BaseAction sortByColumnAnnotationAction = new SortByAnnotationAction(Dimension.COLUMN);

    public static final BaseAction sortByHeader = new SortByHeaderAction();

    public static final BaseAction sortByValueAction = new SortByValueAction();

    public static final BaseAction sortByMutualExclusionAction = new SortByMutualExclusionAction();

    public static final BaseAction hideSelectedRowsAction = new HideSelectionAction(ElementType.ROWS);

    public static final BaseAction hideEmptyLabelHeaderAction = new HideEmptyLabelHeaderAction();

    public static final BaseAction showOnlyHeaderAction = new ShowOnlyLabelHeaderAction();

    public static final BaseAction moveRowsUpAction = new MoveSelectionAction(MoveDirection.ROW_UP);

    public static final BaseAction moveRowsDownAction = new MoveSelectionAction(MoveDirection.ROW_DOWN);

    public static final BaseAction moveColsLeftAction = new MoveSelectionAction(MoveDirection.COL_LEFT);

    public static final BaseAction moveColsRightAction = new MoveSelectionAction(MoveDirection.COL_RIGHT);

    public static final BaseAction clusteringByValueAction = new ClusteringByValueAction();

    //TODO: finish the data integration option
    //public static final BaseAction integrateDataDimensionsAction = new IntegrateDataDimensionsAction();

}

/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions;

import org.gitools.ui.actions.data.*;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.actions.data.HideSelectionAction.ElementType;
import org.gitools.ui.actions.data.MoveSelectionAction.MoveDirection;

public final class DataActions {
	
	public static final BaseAction filterByLabelAction = new FilterByLabelAction();
	
	public static final BaseAction filterByValueAction = new FilterByValueAction();
	
	public static final BaseAction showAllRowsAction = new ShowAllAction(ShowAllAction.ElementType.ROWS);
	
	public static final BaseAction showAllColumnsAction = new ShowAllAction(ShowAllAction.ElementType.COLUMNS);
	
	public static final BaseAction hideSelectedColumnsAction = new HideSelectionAction(ElementType.COLUMNS);
	
	public static final BaseAction fastSortRowsAction = new FastSortRowsAction();

	public static final BaseAction sortByLabelAction = new SortByLabelAction();

	public static final BaseAction sortByValueAction = new SortByValueAction();

	public static final BaseAction sortByMutualExclusionAction = new SortByMutualExclusionAction();
	
	public static final BaseAction hideSelectedRowsAction = new HideSelectionAction(ElementType.ROWS);
	
	public static final BaseAction moveRowsUpAction = new MoveSelectionAction(MoveDirection.ROW_UP);
	
	public static final BaseAction moveRowsDownAction = new MoveSelectionAction(MoveDirection.ROW_DOWN);
	
	public static final BaseAction moveColsLeftAction = new MoveSelectionAction(MoveDirection.COL_LEFT);
	
	public static final BaseAction moveColsRightAction = new MoveSelectionAction(MoveDirection.COL_RIGHT);

	public static final BaseAction clusteringByValueAction = new ClusteringByValueAction();

    public static final BaseAction integrateDataDimensionsAction = new IntegrateDataDimensionsAction();

}

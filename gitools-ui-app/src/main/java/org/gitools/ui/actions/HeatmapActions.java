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

import org.gitools.ui.heatmap.editor.CloneHeatmapAction;
import org.gitools.ui.heatmap.editor.HeatmapSearchAction;
import org.gitools.ui.platform.actions.BaseAction;


public final class HeatmapActions {

	public static final BaseAction cloneAction = new CloneHeatmapAction();

	public static final BaseAction searchAction = new HeatmapSearchAction();
}

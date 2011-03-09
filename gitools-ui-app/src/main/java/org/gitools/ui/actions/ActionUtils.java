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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapAnnotatedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.IEditor;

@Deprecated // TODO we should find a better place/way to do this
public class ActionUtils {

	public static AbstractEditor getSelectedEditor() {
		return AppFrame.instance()
			.getEditorsPanel()
			.getSelectedEditor();
	}

	public static IMatrixView getMatrixView() {
		AbstractEditor editor = getSelectedEditor();
		if (editor == null)
			return null;

		IMatrixView matrixView = null;

		Object model = editor.getModel();
		if (model instanceof IMatrixView)
			matrixView = (IMatrixView) model;
		else if (model instanceof Heatmap)
			matrixView = ((Heatmap)model).getMatrixView();

		return matrixView;
	}

	public static IMatrixView getHeatmapMatrixView() {
		IMatrixView matrixView = null;
		IEditor editor = ActionUtils.getSelectedEditor();
		Object model = editor.getModel();
		if (model instanceof Heatmap)
			matrixView = new HeatmapAnnotatedMatrixView((Heatmap) model);
		else
			matrixView = getMatrixView();
		return matrixView;
	}

	public static Heatmap getHeatmap() {
		AbstractEditor editor = getSelectedEditor();
		if (editor == null)
			return null;

		Heatmap figure = null;

		Object model = editor.getModel();
		if (model instanceof Heatmap)
			figure = (Heatmap)model;

		return figure;
	}
}

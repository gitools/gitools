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

package org.gitools.ui.heatmap.editor;

import java.awt.event.ActionEvent;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.utils.SerialClone;

public class CloneHeatmapAction extends BaseAction {

	public CloneHeatmapAction() {
		super("Clone heatmap");

		setDesc("Clone heatmap");
		setLargeIconFromResource(IconNames.cloneHeatmap24);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IEditor currEd = editorPanel.getSelectedEditor();

		if (!(currEd instanceof HeatmapEditor))
			return;

		HeatmapEditor currentEditor = (HeatmapEditor) currEd;

		Heatmap hm = (Heatmap) currentEditor.getModel();

		Heatmap clone = new Heatmap(new MatrixView(hm.getMatrixView()));
		clone.setTitle(hm.getTitle());
		clone.setDescription(hm.getDescription());
		clone.setAttributes(SerialClone.xclone(hm.getAttributes()));
		clone.setFooter(hm.getFooter());
		clone.setCellHeight(hm.getCellHeight());
		clone.setCellWidth(hm.getCellWidth());
		clone.setShowBorders(hm.isShowBorders());
		clone.setCellDecorator(SerialClone.xclone(hm.getCellDecorator()));
		clone.setColumnDim(SerialClone.xclone(hm.getColumnDim()));
		clone.setRowDim(SerialClone.xclone(hm.getRowDim()));

		HeatmapEditor editor = new HeatmapEditor(clone, currentEditor.getExternalToolbarActions());

		editor.setName(editorPanel.deriveName(
				currentEditor.getName(), FileSuffixes.HEATMAP,
				"", FileSuffixes.HEATMAP));

		editorPanel.addEditor(editor);
	}
}

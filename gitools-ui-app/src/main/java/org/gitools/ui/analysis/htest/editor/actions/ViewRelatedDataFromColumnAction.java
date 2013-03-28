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

package org.gitools.ui.analysis.htest.editor.actions;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.ModuleMap;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;


public class ViewRelatedDataFromColumnAction extends BaseAction {

	protected String title;
	protected IMatrix matrix;
	protected ModuleMap map;

	public ViewRelatedDataFromColumnAction(
			String title, IMatrix matrix, ModuleMap map) {
		super("View annotated elements");

		setDesc("View annotated elements in a new heatmap");
		setLargeIconFromResource(IconNames.viewAnnotatedElements24);
		setSmallIconFromResource(IconNames.viewAnnotatedElements16);

		this.title = title;
		this.matrix = matrix;
		this.map = map;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IEditor currentEditor = editorPanel.getSelectedEditor();

		// Check selection
		Heatmap srcHeatmap = (Heatmap) currentEditor.getModel();
		IMatrixView srcMatrixView = srcHeatmap.getMatrixView();
		IMatrix srcMatrix = srcMatrixView.getContents();
		int[] selColumns = srcMatrixView.getSelectedColumns();
		int leadColumn = srcMatrixView.getLeadSelectionColumn();
		if ((selColumns == null || selColumns.length == 0) && leadColumn != -1)
			selColumns = new int[] {leadColumn};
		else if (leadColumn == -1) {
			JOptionPane.showMessageDialog(AppFrame.instance(),
				"You must select some columns before.",
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Retrieve elements
		int[] view = srcMatrixView.getVisibleColumns();

		Set<Integer> elements = new HashSet<Integer>();

		Map<String, Integer> itemNameMap = new HashMap<String, Integer>();
		for (int i = 0; i < matrix.getColumnCount(); i++)
			itemNameMap.put(matrix.getColumnLabel(i), i);

		StringBuilder moduleNames = new StringBuilder();

		for (int i = 0; i < selColumns.length; i++) {
			String modName = srcMatrix.getColumnLabel(view[selColumns[i]]);
			if (i != 0)
				moduleNames.append(", ");
			moduleNames.append(modName);

			int[] indices = map.getItemIndices(modName);
			if (indices != null)
				for (int index : indices) {
					String itemName = map.getItemName(index);
					Integer dstIndex = itemNameMap.get(itemName);
					if (dstIndex != null)
						elements.add(dstIndex);
				}
		}

		int[] newView = new int[elements.size()];
		int i = 0;
		for (Integer index : elements)
			newView[i++] = index;

		// Create heatmap
		final IMatrixView matrixView = new MatrixView(matrix);
		matrixView.setVisibleColumns(newView);

		Heatmap heatmap = HeatmapUtil.createFromMatrixView(matrixView);
		heatmap.setTitle(title);
		heatmap.setDescription("Annotated elements for column sets: " + moduleNames.toString());

		// Create editor
		HeatmapEditor editor = new HeatmapEditor(heatmap);

		editor.setName(editorPanel.deriveName(
				currentEditor.getName(), FileSuffixes.HEATMAP,
				"-data", FileSuffixes.HEATMAP));
		
		editorPanel.addEditor(editor);

		AppFrame.instance().setStatusText("New heatmap created.");
	}

}

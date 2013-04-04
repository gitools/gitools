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
package org.gitools.ui.analysis.htest.editor.actions;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.formats.analysis.HeatmapFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ViewRelatedDataFromColumnAction extends BaseAction
{

    protected String title;
    protected IMatrix matrix;
    protected ModuleMap map;

    public ViewRelatedDataFromColumnAction(
            String title, IMatrix matrix, ModuleMap map)
    {
        super("View annotated elements");

        setDesc("View annotated elements in a new heatmap");
        setLargeIconFromResource(IconNames.viewAnnotatedElements24);
        setSmallIconFromResource(IconNames.viewAnnotatedElements16);

        this.title = title;
        this.matrix = matrix;
        this.map = map;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        IEditor currentEditor = editorPanel.getSelectedEditor();

        // Check selection
        Heatmap srcHeatmap = (Heatmap) currentEditor.getModel();
        IMatrixView srcMatrixView = srcHeatmap.getMatrixView();
        IMatrix srcMatrix = srcMatrixView.getContents();
        int[] selColumns = srcMatrixView.getSelectedColumns();
        int leadColumn = srcMatrixView.getLeadSelectionColumn();
        if ((selColumns == null || selColumns.length == 0) && leadColumn != -1)
        {
            selColumns = new int[]{leadColumn};
        }
        else if (leadColumn == -1)
        {
            JOptionPane.showMessageDialog(AppFrame.get(),
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

        for (int i = 0; i < selColumns.length; i++)
        {
            String modName = srcMatrix.getColumnLabel(view[selColumns[i]]);
            if (i != 0)
            {
                moduleNames.append(", ");
            }
            moduleNames.append(modName);

            int[] indices = map.getItemIndices(modName);
            if (indices != null)
            {
                for (int index : indices)
                {
                    String itemName = map.getItemName(index);
                    Integer dstIndex = itemNameMap.get(itemName);
                    if (dstIndex != null)
                    {
                        elements.add(dstIndex);
                    }
                }
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
                currentEditor.getName(), HeatmapFormat.EXTENSION,
                "-data", HeatmapFormat.EXTENSION));

        editorPanel.addEditor(editor);

        AppFrame.get().setStatusText("New heatmap created.");
    }

}

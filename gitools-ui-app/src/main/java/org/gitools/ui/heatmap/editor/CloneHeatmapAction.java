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
package org.gitools.ui.heatmap.editor;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.DiagonalMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.persistence.formats.analysis.HeatmapFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.utils.SerialClone;

import java.awt.event.ActionEvent;

public class CloneHeatmapAction extends BaseAction
{

    public CloneHeatmapAction()
    {
        super("Clone heatmap");

        setDesc("Clone heatmap");
        setLargeIconFromResource(IconNames.cloneHeatmap24);
    }

    @Override
    public boolean updateEnabledByEditor(IEditor editor)
    {
        setEnabled(editor instanceof HeatmapEditor);
        return isEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        IEditor currEd = editorPanel.getSelectedEditor();

        if (!(currEd instanceof HeatmapEditor))
        {
            return;
        }

        HeatmapEditor currentEditor = (HeatmapEditor) currEd;

        Heatmap hm = (Heatmap) currentEditor.getModel();

        IMatrixView mv = hm;
        if (mv instanceof DiagonalMatrixView)
        {
            mv = new DiagonalMatrixView(mv);
        }
        Heatmap clone = new Heatmap(mv);
        clone.setTitle(hm.getTitle());
        clone.setDescription(hm.getDescription());
        clone.setProperties(SerialClone.xclone(hm.getProperties()));
        clone.setColumns(SerialClone.xclone(hm.getColumns()));
        clone.setRows(SerialClone.xclone(hm.getRows()));

        HeatmapEditor editor = new HeatmapEditor(clone);

        editor.setName(editorPanel.deriveName(currentEditor.getName(), HeatmapFormat.EXTENSION, "", HeatmapFormat.EXTENSION));

        editorPanel.addEditor(editor);
    }
}

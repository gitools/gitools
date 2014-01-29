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
package org.gitools.ui.app.analysis.htest.editor;

import static com.google.common.base.Predicates.in;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.model.IModuleMap;
import org.gitools.persistence.formats.analysis.HeatmapFormat;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.editor.EditorsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;


public class ViewRelatedDataFromAction extends HeatmapDimensionAction {

    private final String title;
    private final IMatrix matrix;
    private final IModuleMap map;

    public ViewRelatedDataFromAction(String title, IMatrix matrix, IModuleMap map, MatrixDimensionKey dimension) {
        super(dimension, "View annotated elements in a new heatmap");

        setLargeIconFromResource(IconNames.viewAnnotatedElements24);
        setSmallIconFromResource(IconNames.viewAnnotatedElements16);

        this.title = title;
        this.matrix = matrix;
        this.map = map;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Check selection
        Heatmap srcHeatmap = getHeatmap();
        Set<String> selection = getDimension().getSelected();
        String lead = getDimension().getFocus();

        if ((selection == null || selection.size() == 0) && lead != null) {
            selection = Sets.newHashSet(lead);
        } else if (lead == null) {
            JOptionPane.showMessageDialog(Application.get(), "You must select some " + getDimensionLabel() + "s before.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Build new heatmap
        Heatmap heatmap = new Heatmap(matrix);
        getDimension().show(in(selection));
        heatmap.setTitle(title);
        heatmap.setDescription("Annotated elements for " + getDimensionLabel() + " sets: " + StringUtils.join(selection, ", "));

        // Open new editor
        HeatmapEditor editor = new HeatmapEditor(heatmap);
        EditorsPanel editorPanel = Application.get().getEditorsPanel();
        editor.setName(editorPanel.deriveName(editorPanel.getSelectedEditor().getName(), HeatmapFormat.EXTENSION, "-data", HeatmapFormat.EXTENSION));
        editorPanel.addEditor(editor);
        Application.get().setStatusText("New heatmap created.");
    }

}

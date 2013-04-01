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
package org.gitools.ui.analysis.htest.editor;

import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.gitools.ui.analysis.htest.editor.actions.ViewRelatedDataFromColumnAction;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OncodriveResultsEditor extends HeatmapEditor
{

    protected OncodriveAnalysis analysis;

    protected AbstractTablesPanel tablesPanel;

    protected static Heatmap createHeatmap(OncodriveAnalysis analysis)
    {
        IMatrixView dataTable = new MatrixView(analysis.getResults());
        Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }

    protected static List<BaseAction> createToolBar(OncodriveAnalysis analysis)
    {
        ViewRelatedDataFromColumnAction action =
                new ViewRelatedDataFromColumnAction(
                        analysis.getTitle(),
                        analysis.getData(),
                        analysis.getModuleMap());
        List<BaseAction> tb = new ArrayList<BaseAction>();
        tb.add(action);
        return tb;
    }

    public OncodriveResultsEditor(OncodriveAnalysis analysis)
    {
        super(createHeatmap(analysis), createToolBar(analysis), true);

        tablesPanel = new OncodriveTablesPanel(analysis, heatmap);
        tablesPanel.setMinimumSize(new Dimension(140, 140));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(1);
        splitPane.setOneTouchExpandable(true);
        splitPane.setTopComponent(embeddedContainer);
        splitPane.setBottomComponent(tablesPanel);

        setLayout(new BorderLayout());
        add(splitPane);
    }
}

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
package org.gitools.ui.analysis.combination.editor;


import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.heatmap.editor.HeatmapEditor;

import javax.swing.*;
import java.awt.*;

public class CombinationResultsEditor extends HeatmapEditor
{

    protected CombinationAnalysis analysis;

    protected CombinationTablesPanel tablesPanel;

    protected static Heatmap createHeatmap(CombinationAnalysis analysis)
    {
        IMatrixView dataTable = new MatrixView(analysis.getResults().get());
        Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }

    public CombinationResultsEditor(CombinationAnalysis analysis)
    {
        super(createHeatmap(analysis), true);

        tablesPanel = new CombinationTablesPanel(analysis, heatmap);
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

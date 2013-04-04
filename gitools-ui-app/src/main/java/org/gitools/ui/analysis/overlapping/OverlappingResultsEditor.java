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
package org.gitools.ui.analysis.overlapping;

import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.DiagonalMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.impl.LinearTwoSidedElementDecorator;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;


public class OverlappingResultsEditor extends HeatmapEditor
{

    //private OverlappingAnalysis analysis;

    protected AbstractTablesPanel tablesPanel;

    @Nullable
    protected static Heatmap createHeatmap(@NotNull OverlappingAnalysis analysis)
    {
        IMatrixView results = new DiagonalMatrixView(analysis.getCellResults().get());
        Heatmap heatmap = new Heatmap(results);
        heatmap.setTitle(analysis.getTitle() + " (results)");
        IElementAdapter cellAdapter = results.getCellAdapter();
        int propertiesNb = cellAdapter.getProperties().size();
        LinearTwoSidedElementDecorator[] dec = new LinearTwoSidedElementDecorator[propertiesNb];
        for (int i = 0; i < propertiesNb; i++)
        {
            dec[i] = new LinearTwoSidedElementDecorator(cellAdapter);
            int valueIndex = cellAdapter.getPropertyIndex("jaccard-index");
            Color minColor = new Color(0x63, 0xdc, 0xfe);
            Color maxColor = new Color(0xff, 0x00, 0x5f);
            dec[i].setValueIndex(valueIndex != -1 ? valueIndex : 0);
            dec[i].setMinValue(0.0);
            dec[i].setMinColor(minColor);
            dec[i].setMidValue(1.0);
            dec[i].setMidColor(maxColor);
            dec[i].setMaxValue(1.0);
            dec[i].setMaxColor(maxColor);
            dec[i].setEmptyColor(Color.WHITE);
        }
        heatmap.setCellDecorators(dec);

        heatmap.setTitle(analysis.getTitle());

        return heatmap;
    }

    public OverlappingResultsEditor(@NotNull OverlappingAnalysis analysis)
    {
        super(createHeatmap(analysis), true);

        tablesPanel = new OverlappingTablesPanel(analysis, heatmap);
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

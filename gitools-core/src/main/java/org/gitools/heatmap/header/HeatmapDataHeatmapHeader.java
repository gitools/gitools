/*
 * #%L
 * gitools-core
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
package org.gitools.heatmap.header;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapDataHeatmapHeader extends HeatmapHeader
{

    public enum LabelPositionEnum
    {
        leftOf,
        rightOf,
        inside
    }

    private static final String HEADER_HEATMAP_CHANGED = "headerHeatmap";
    private LabelPositionEnum labelPosition;

    private Heatmap headerHeatmap;
    private Map<String, Integer> labelIndexMap;

    private boolean forceLabelColor;

    public HeatmapDataHeatmapHeader()
    {
        super();
    }

    public HeatmapDataHeatmapHeader(HeatmapDimension hdim)
    {
        super(hdim);

        size = 80;

        this.labelPosition = LabelPositionEnum.inside;
        labelColor = Color.BLACK;
        forceLabelColor = false;

    }

    public void setHeaderHeatmap(Heatmap headerHeatmap)
    {
        Heatmap old = this.headerHeatmap;
        this.headerHeatmap = headerHeatmap;
        firePropertyChange(HEADER_HEATMAP_CHANGED, old, headerHeatmap);
    }

    public Heatmap getHeaderHeatmap()
    {
        return this.headerHeatmap;
    }

    public Map<String, Integer> getLabelIndexMap()
    {
        return labelIndexMap;
    }

    public void setLabelIndexMap(Map<String, Integer> labelIndexMap)
    {
        this.labelIndexMap = labelIndexMap;
    }

    public LabelPositionEnum getLabelPosition()
    {
        return labelPosition;
    }

    public void setLabelPosition(LabelPositionEnum labelPosition)
    {
        this.labelPosition = labelPosition;
    }

    public boolean isForceLabelColor()
    {
        return forceLabelColor;
    }

    public void setForceLabelColor(boolean forceLabelColor)
    {
        this.forceLabelColor = forceLabelColor;
    }

    @Override
    public void updateLargestLabelLength(@NotNull Component component)
    {
        // Get largest label:
        if (headerHeatmap == null)
        {
            return;
        }

        int rows = headerHeatmap.getRows().size();
        int cols = headerHeatmap.getColumns().size();
        IMatrixView data = headerHeatmap;

        // Formatter for value labels
        GenericFormatter gf = new GenericFormatter();
        FontMetrics fm = component.getFontMetrics(this.font);

        int largestLabelLenght = 0;
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                Double element = (Double) data.getCellValue(row, col, data.getLayers().getTopLayer());
                String valueLabel = gf.format(element);
                int length = fm.stringWidth(valueLabel);
                if (length > largestLabelLenght)
                {
                    largestLabelLenght = length;
                }
            }
        }
        setLargestLabelLength(largestLabelLenght);
    }

    @Override
    public String[] getAnnotationValues(boolean horizontal)
    {

        String[] values;
        if (horizontal)
        {
            int size = headerHeatmap.getColumns().size();
            values = new String[size];
            for (int i = 0; i < size; i++)
            {
                values[i] = headerHeatmap.getColumns().getLabel(i);
            }
        }
        else
        {
            int size = headerHeatmap.getRows().size();
            values = new String[size];
            for (int i = 0; i < size; i++)
                values[i] = headerHeatmap.getRows().getLabel(i);
        }

        return values;
    }

}

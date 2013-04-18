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
import org.gitools.model.decorator.Decorator;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.NONE)
public class HeatmapDataHeatmapHeader extends HeatmapHeader {

    public enum LabelPositionEnum {
        leftOf,
        rightOf,
        inside
    }

    private static final String HEADER_HEATMAP_CHANGED = "headerHeatmap";

    @XmlElement(name = "label-position")
    private LabelPositionEnum labelPosition;

    @XmlTransient
    private Heatmap heatmap;

    @XmlElement
    private List<LabelIndex> labelIndices;
    private transient Map<String, Integer> labelIndexMa;

    @XmlElement(name = "force-label-color")
    private boolean forceLabelColor;

    @XmlElement
    private Decorator decorator;

    public HeatmapDataHeatmapHeader() {
        super();
    }

    public HeatmapDataHeatmapHeader(HeatmapDimension hdim) {
        super(hdim);

        size = 80;
        this.labelPosition = LabelPositionEnum.inside;
        labelColor = Color.BLACK;
        forceLabelColor = false;

    }

    public void setHeatmap(Heatmap heatmap) {
        Heatmap old = this.heatmap;
        this.heatmap = heatmap;
        firePropertyChange(HEADER_HEATMAP_CHANGED, old, heatmap);
    }

    public Heatmap getHeatmap() {
        return this.heatmap;
    }

    public Map<String, Integer> getLabelIndexMap() {
        if (labelIndexMa == null) {
            labelIndexMa = new HashMap<String, Integer>();
            for (LabelIndex labelIndex : labelIndices) {
                labelIndexMa.put(labelIndex.getLabel(), labelIndex.getIndex());
            }
        }
        return labelIndexMa;
    }

    public void setLabelIndexMap(Map<String, Integer> labelIndexMap) {
        labelIndices = new ArrayList<LabelIndex>();
        for (Map.Entry<String, Integer> entry : labelIndexMap.entrySet()) {
            labelIndices.add(new LabelIndex(entry.getKey(), entry.getValue()));
        }

        this.labelIndexMa = labelIndexMap;
    }

    public LabelPositionEnum getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(LabelPositionEnum labelPosition) {
        this.labelPosition = labelPosition;
    }

    public boolean isForceLabelColor() {
        return forceLabelColor;
    }

    public void setForceLabelColor(boolean forceLabelColor) {
        this.forceLabelColor = forceLabelColor;
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    @Override
    public void updateLargestLabelLength(@NotNull Component component) {
        // Get largest label:
        if (heatmap == null) {
            return;
        }

        /*TODO
        int rows = heatmap.getRows().size();
        int cols = heatmap.getColumns().size();
        IMatrixView data = heatmap;

        // Formatter for value labels
        GenericFormatter gf = new GenericFormatter();
        FontMetrics fm = component.getFontMetrics(this.font);

        int largestLabelLenght = 0;
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                Double element = (Double) data.getCellValue(row, col, data.getLayers().getTopLayerIndex());
                String valueLabel = gf.format(element);
                int length = fm.stringWidth(valueLabel);
                if (length > largestLabelLenght)
                {
                    largestLabelLenght = length;
                }
            }
        }
        setLargestLabelLength(largestLabelLenght);
        */
    }

    @Override
    public String[] getAnnotationValues(boolean horizontal) {

        String[] values;
        if (horizontal) {
            int size = heatmap.getColumns().size();
            values = new String[size];
            for (int i = 0; i < size; i++) {
                values[i] = heatmap.getColumns().getLabel(i);
            }
        } else {
            int size = heatmap.getRows().size();
            values = new String[size];
            for (int i = 0; i < size; i++)
                values[i] = heatmap.getRows().getLabel(i);
        }

        return values;
    }

    @Override
    public void init(Heatmap heatmap) {
        this.heatmap = heatmap;
    }
}

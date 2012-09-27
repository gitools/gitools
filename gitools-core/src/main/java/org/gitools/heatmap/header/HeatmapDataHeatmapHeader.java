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

package org.gitools.heatmap.header;

import edu.upf.bg.formatter.GenericFormatter;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.matrix.model.IMatrixView;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HeatmapDataHeatmapHeader extends HeatmapHeader {

    public enum LabelPositionEnum {
        leftOf,
        rightOf,
        inside;
    }

    private static final String HEADER_HEATMAP_CHANGED = "headerHeatmap";
    private LabelPositionEnum labelPosition;

    private Heatmap headerHeatmap;
    private Map<String,Integer> labelIndexMap;

    private boolean forceLabelColor;

    public HeatmapDataHeatmapHeader(HeatmapDim hdim) {
		super(hdim);

		size = 80;

        this.labelPosition = LabelPositionEnum.inside;
        labelColor = Color.BLACK;
        forceLabelColor = false;

	}

    public void setHeaderHeatmap (Heatmap headerHeatmap) {
        Heatmap old = this.headerHeatmap;
        this.headerHeatmap = headerHeatmap;
        firePropertyChange(HEADER_HEATMAP_CHANGED,old,headerHeatmap);
    }

    public Heatmap getHeaderHeatmap () {
        return this.headerHeatmap;
    }

    public Map<String,Integer> getLabelIndexMap() {
        return labelIndexMap;
    }

    public void setLabelIndexMap(Map<String,Integer> labelIndexMap) {
        this.labelIndexMap = labelIndexMap;
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

    @Override
    public void updateLargestLabelLength(Component component) {
        // Get largest label:
        if (headerHeatmap == null)
            return;
        
        int rows = headerHeatmap.getMatrixView().getRowCount();
        int cols = headerHeatmap.getMatrixView().getColumnCount();
        IMatrixView  data = headerHeatmap.getMatrixView();

        // Formatter for value labels
        GenericFormatter gf = new GenericFormatter();
        FontMetrics fm = component.getFontMetrics(this.font);

        int largestLabelLenght = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Double element = (Double) data.getCell(row, col);
                String valueLabel = gf.format(element);
                int length = fm.stringWidth(valueLabel);
                if (length > largestLabelLenght)
                    largestLabelLenght = length;
            }
        }
        setLargestLabelLength(largestLabelLenght);
    }

}

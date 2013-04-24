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
package org.gitools.core.heatmap.header;

import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.model.decorator.Decoration;
import org.gitools.core.model.decorator.Decorator;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
public class HeatmapDecoratorHeader extends HeatmapHeader {

    public enum LabelPositionEnum {
        leftOf,
        rightOf,
        inside
    }

    @XmlElement(name = "label-position")
    private LabelPositionEnum labelPosition;

    @XmlElement(name = "force-label-color")
    private boolean forceLabelColor;

    @XmlElement
    private Decorator decorator;

    @XmlElement
    private List<String> annotationLabels;

    public HeatmapDecoratorHeader() {
        super();
    }

    public HeatmapDecoratorHeader(HeatmapDimension heatmapDimension) {
        super(heatmapDimension);

        size = 80;
        this.labelPosition = LabelPositionEnum.inside;
        labelColor = Color.BLACK;
        forceLabelColor = false;
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

    public List<String> getAnnotationLabels() {
        return annotationLabels;
    }

    public void setAnnotationLabels(List<String> annotationLabels) {
        this.annotationLabels = annotationLabels;
    }

    public void decorate(Decoration decoration, int index, String annotation) {
        decorator.decorate(decoration, getMatrixAdapter(), index, index, getMatrixAdapter().indexOf(annotation));
    }

    private MatrixAdapter matrixAdapter;

    private MatrixAdapter getMatrixAdapter() {

        if (matrixAdapter == null) {
            matrixAdapter = new MatrixAdapter(this);
        }

        return matrixAdapter;
    }


    @Override
    public void updateLargestLabelLength(@NotNull Component component) {
        // Get largest label:

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

}

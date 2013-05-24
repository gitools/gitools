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
import org.gitools.core.label.AnnotationProvider;
import org.gitools.core.label.LabelProvider;
import org.gitools.core.model.decorator.Decoration;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.core.model.decorator.DetailsDecoration;

import javax.xml.bind.annotation.*;
import java.awt.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
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

    @XmlTransient
    private String sortLabel;

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

    public void decorate(Decoration decoration, int index, String annotation, boolean forceShowLabel) {

        boolean showValue = decorator.isShowValue();
        if (forceShowLabel) {
            decorator.setShowValue(true);
        }
        decorator.decorate(decoration, getMatrixAdapter(), index, index, getMatrixAdapter().indexOf(annotation));
        decorator.setShowValue(showValue);
    }

    private MatrixAdapter matrixAdapter;

    private MatrixAdapter getMatrixAdapter() {

        if (matrixAdapter == null) {
            matrixAdapter = new MatrixAdapter(this);
        }

        return matrixAdapter;
    }

    @Override
    public void populateDetails(List<DetailsDecoration> details, int index) {

        String intersection = null;

        for (String annotation : getAnnotationLabels()) {
            if (intersection == null) {
                intersection = annotation;
            } else {

                intersection = getIntersectionAtBeginning(intersection, annotation);

            }
        }

        for (String annotation : getAnnotationLabels()) {

            String trimmed = annotation.replaceFirst(intersection, "");
            if (trimmed.length() > 0) {
                trimmed = " (" + trimmed + ")";
            }

            DetailsDecoration decoration = new DetailsDecoration(getTitle() + trimmed, getDescription(), getDescriptionUrl(), null, getValueUrl());

            if (index != -1) {
                decorate(decoration, index, annotation, true);
            }

            details.add(decoration);
        }

    }

    private String getIntersectionAtBeginning(String s1, String s2) {
        int max = s1.length() > s2.length() ?
                s2.length() : s1.length();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        boolean equal = true;
        while (i < max && equal) {
            equal = s1.charAt(i) == s2.charAt(i);
            if (equal) {
                sb.append(s2.charAt(i));
            }
            i++;
        }
        return sb.toString();
    }

    @Override
    public LabelProvider getLabelProvider() {
        return new AnnotationProvider(getHeatmapDimension(), sortLabel);
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public void setSortLabel(String label) {

        if (annotationLabels.contains(label)) {
            sortLabel = label;
        } else {
            sortLabel = annotationLabels.get(0);
        }

    }

    @Override
    public boolean isNumeric() {
        return true;
    }
}

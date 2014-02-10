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

import com.google.common.base.Function;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.matrix.filter.AnnotationFunction;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.utils.formatter.HeatmapTextFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
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

    public void setAnnotationLabels(Collection<String> annotationLabels) {
        this.annotationLabels = new ArrayList<>(annotationLabels);
    }

    public void decorate(Decoration decoration, String identifier, String annotation, boolean forceShowLabel) {

        boolean showValue = decorator.isShowValue();
        if (forceShowLabel) {
            decorator.setShowValue(true);
        }

        IAnnotations annotations = getHeatmapDimension().getAnnotations();

        decorator.decorate(
                decoration,
                HeatmapTextFormatter.TWO_DECIMALS,
                annotations,
                annotations.getLayers().get(annotation),
                identifier);
        decorator.setShowValue(showValue);
    }

    @Override
    public void populateDetails(List<DetailsDecoration> details, String index) {

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
            decoration.setReference(this);

            if (index != null) {
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
    public Function<String, String> getIdentifierTransform() {
        return new AnnotationFunction(sortLabel, getHeatmapDimension().getAnnotations());
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

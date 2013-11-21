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

import com.google.common.base.Function;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.matrix.filter.NoTransformFunction;
import org.gitools.core.matrix.filter.AnnotationFunction;
import org.gitools.core.matrix.filter.PatternFunction;
import org.gitools.core.model.decorator.DetailsDecoration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class HeatmapTextLabelsHeader extends HeatmapHeader {

    private static final String LABEL_PATTERN_CHANGED = "labelPatternChanged";

    public enum LabelSource {
        ID, ANNOTATION, PATTERN
    }

    @XmlElement(name = "label-source")
    private LabelSource labelSource;

    @XmlElement(name = "label-annotation")
    private String labelAnnotation;

    @XmlElement(name = "label-pattern")
    private String labelPattern;

    public HeatmapTextLabelsHeader() {
        this(null);
    }

    public HeatmapTextLabelsHeader(HeatmapDimension hdim) {
        super(hdim);

        size = 80;
        labelColor = Color.darkGray;
        backgroundColor = Color.WHITE;
        font = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
        IAnnotations am = hdim != null ? hdim.getAnnotations() : null;
        if (am != null && am.getLabels().size() > 0) {
            labelSource = LabelSource.ANNOTATION;
            labelAnnotation = am.getLabels().iterator().next();
        } else {
            labelSource = LabelSource.ID;
            labelAnnotation = "";
        }

        labelPattern = "${id}";
    }


    @Override
    public String getTitle() {
        StringBuilder sb = new StringBuilder();
        switch (labelSource) {
            case ID:
                sb.append("ID");
                break;
            case ANNOTATION:
                sb.append(labelAnnotation);
                break;
            case PATTERN:
                sb.append(labelPattern);
                break;
        }
        return sb.toString();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        Font old = this.font;
        this.font = font;
        firePropertyChange(PROPERTY_FONT, old, font);
    }

    public LabelSource getLabelSource() {
        return labelSource;
    }

    public void setLabelSource(LabelSource labelSource) {
        this.labelSource = labelSource;
    }

    public String getLabelAnnotation() {
        return labelAnnotation;
    }

    public void setLabelAnnotation(String labelAnnotation) {
        this.labelAnnotation = labelAnnotation;
    }

    public String getLabelPattern() {
        return labelPattern;
    }

    public void setLabelPattern(String pattern) {
        String old = this.labelPattern;
        this.labelPattern = pattern;
        firePropertyChange(LABEL_PATTERN_CHANGED, old, pattern);
    }

    @Override
    public void populateDetails(List<DetailsDecoration> details, String identifier) {
        details.add(
                new DetailsDecoration(
                        getTitle(),
                        getDescription(),
                        getDescriptionUrl(),
                        getIdentifierTransform().apply(identifier),
                        getValueUrl()
                )
        );
    }

    private transient Function<String, String> transformFunction;

    public Function<String, String> getIdentifierTransform() {

        if (transformFunction == null) {
            switch (getLabelSource()) {
                case ANNOTATION:
                    transformFunction = new AnnotationFunction(getLabelAnnotation(), getHeatmapDimension().getAnnotations());
                    break;
                case PATTERN:
                    transformFunction = new PatternFunction(getLabelPattern(), getHeatmapDimension().getAnnotations());
                    break;
                default:
                    transformFunction = new NoTransformFunction<>();
            }
        }

        return transformFunction;
    }

}

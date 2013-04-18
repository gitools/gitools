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
import org.gitools.model.AbstractModel;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.gitools.utils.xml.adapter.FontXmlAdapter;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({HeatmapColoredLabelsHeader.class, HeatmapDataHeatmapHeader.class, HeatmapHierarchicalColoredLabelsHeader.class, HeatmapTextLabelsHeader.class})
public abstract class HeatmapHeader extends AbstractModel {
    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_SIZE = "size";
    public static final String PROPERTY_VISIBLE = "visible";
    public static final String PROPERTY_BACKGROUND_COLOR = "backgroundColor";
    public static final String PROPERTY_MARGIN = "margin";
    public static final String PROPERTY_LABEL_VISIBLE = "labelVisible";
    public static final String PROPERTY_FONT = "labelFont";
    public static final String PROPERTY_LABEL_ROTATED = "labelRotated";
    public static final String PROPERTY_LABEL_COLOR = "labelColor";
    public static final String PROPERTY_LARGEST_LABEL_LENGTH = "largestLabelLength";

    @XmlElement
    private String title;

    @XmlElement
    protected int size;

    @XmlElement
    private boolean visible;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    @XmlElement(name = "background-color")
    protected Color backgroundColor;

    @XmlElement
    int margin;

    @XmlElement(name = "label-visible")
    protected boolean labelVisible;

    @XmlJavaTypeAdapter(FontXmlAdapter.class)
    protected Font font;

    @XmlElement(name = "label-rotated")
    protected boolean labelRotated;

    @Nullable
    @XmlElement(name = "annotation-pattern")
    private String annotationPattern;

    @XmlElement(name = "annotation-values")
    private String[] annotationValues;

    @XmlElement(name = "largest-label")
    protected int largestLabelLength;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    @XmlElement(name = "label-color")
    protected Color labelColor;

    public HeatmapHeader() {

    }

    HeatmapHeader(HeatmapDimension dim) {
        this.dim = dim;
        this.title = "";
        this.size = 100;
        this.visible = true;
        this.backgroundColor = Color.WHITE;
        font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
        margin = 1;
        labelRotated = false;
        labelVisible = false;
        labelColor = Color.GRAY;
        annotationPattern = null;
        annotationValues = new String[0];
    }

    @XmlTransient
    private HeatmapDimension dim;

    public HeatmapDimension getHeatmapDim() {
        return dim;
    }

    public void setHeatmapDim(HeatmapDimension dim) {
        this.dim = dim;
    }

    /**
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * The title
     */
    public void setTitle(String title) {
        String old = this.title;
        this.title = title;
        firePropertyChange(PROPERTY_TITLE, old, title);
    }

    /**
     * The height/width
     */
    public int getSize() {
        return size;
    }

    /**
     * The height/width
     */
    public void setSize(int size) {
        int old = this.size;
        this.size = size;
        firePropertyChange(PROPERTY_SIZE, old, size);
    }

    /**
     * Wether the header is visible
     */
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean old = this.visible;
        this.visible = visible;
        firePropertyChange(PROPERTY_VISIBLE, old, visible);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color color) {
        Color old = this.backgroundColor;
        this.backgroundColor = color;
        firePropertyChange(PROPERTY_BACKGROUND_COLOR, old, color);
    }

    /* Color band margin */
    public int getMargin() {
        return margin;
    }

    /* Color band margin */
    public void setMargin(int margin) {
        int old = this.margin;
        this.margin = margin;
        firePropertyChange(PROPERTY_MARGIN, old, margin);
    }

    /**
     * Whether to show labels of each value
     */
    public void setLabelVisible(boolean labelVisible) {
        boolean old = this.labelVisible;
        this.labelVisible = labelVisible;
        firePropertyChange(PROPERTY_LABEL_VISIBLE, old, labelVisible);
    }

    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * The font to use for labels
     */
    public Font getLabelFont() {
        return font;
    }

    /**
     * The font to use for labels
     */
    public void setLabelFont(Font font) {
        Font old = this.font;
        this.font = font;
        updateLargestLabelLength(null);
        firePropertyChange(PROPERTY_FONT, old, font);
    }

    /**
     * If false the label is painted along the color band,
     * otherwise the label is perpendicular to the color band
     */
    public boolean isLabelRotated() {
        return labelRotated;
    }

    /**
     * If false the label is painted along the color band,
     * otherwise the label is perpendicular to the color band
     */
    public void setLabelRotated(boolean labelRotated) {
        boolean old = this.labelRotated;
        this.labelRotated = labelRotated;
        firePropertyChange(PROPERTY_LABEL_ROTATED, old, labelRotated);
    }

    /**
     * Label foreground color
     */
    public Color getLabelColor() {
        return labelColor;
    }

    /**
     * Label foreground color
     */
    public void setLabelColor(Color labelColor) {
        Color old = this.labelColor;
        this.labelColor = labelColor;
        firePropertyChange(PROPERTY_LABEL_COLOR, old, labelColor);
    }

    public int getLargestLabelLength() {
        return largestLabelLength;
    }

    void setLargestLabelLength(int largestLabelLength) {
        int old = this.largestLabelLength;
        this.largestLabelLength = largestLabelLength;
        firePropertyChange(PROPERTY_LARGEST_LABEL_LENGTH, old, largestLabelLength);
    }

    @Nullable
    public String getAnnotationPattern() {
        return annotationPattern;
    }

    public void setAnnotationPattern(String annotationPattern) {
        this.annotationPattern = annotationPattern;
    }

    public String[] getAnnotationValues(boolean horizontal) {
        return annotationValues;
    }

    public void setAnnotationValues(String[] annotationValues) {
        this.annotationValues = annotationValues;
    }

    abstract protected void updateLargestLabelLength(Component component);

    public void init(Heatmap heatmap) {

    }

}

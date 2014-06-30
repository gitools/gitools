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
import com.jgoodies.binding.beans.Model;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.gitools.utils.xml.adapter.FontXmlAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({HeatmapColoredLabelsHeader.class, HeatmapDecoratorHeader.class, HeatmapTextLabelsHeader.class,
HierarchicalClusterHeatmapHeader.class})
public abstract class HeatmapHeader extends Model {
    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_SIZE = "size";
    public static final String PROPERTY_VISIBLE = "visible";
    public static final String PROPERTY_BACKGROUND_COLOR = "backgroundColor";
    public static final String PROPERTY_MARGIN = "margin";
    public static final String PROPERTY_LABEL_VISIBLE = "labelVisible";
    public static final String PROPERTY_FONT = "labelFont";
    public static final String PROPERTY_LABEL_ROTATED = "labelRotated";
    public static final String PROPERTY_LABEL_COLOR = "labelColor";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_DESCRIPTION_URL = "descriptionUrl";
    public static final String PROPERTY_VALUE_URL = "valueUrl";

    @XmlElement
    private String title;

    @XmlElement
    private String description;

    @XmlElement(name = "description-link")
    private String descriptionUrl;

    @XmlElement(name = "value-link")
    private String valueUrl;

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


    @XmlElement(name = "annotation-pattern")
    private String annotationPattern;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    @XmlElement(name = "label-color")
    protected Color labelColor;

    @XmlTransient
    private boolean sortAscending = true;

    public HeatmapHeader() {

    }

    public HeatmapHeader(HeatmapDimension heatmapDimension) {
        this.heatmapDimension = heatmapDimension;
        this.title = "";
        this.size = 100;
        this.visible = true;
        this.backgroundColor = Color.WHITE;
        font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
        margin = 5;
        labelRotated = false;
        labelVisible = false;
        labelColor = Color.GRAY;
        annotationPattern = null;
    }

    @XmlTransient
    private HeatmapDimension heatmapDimension;

    public HeatmapDimension getHeatmapDimension() {
        return heatmapDimension;
    }

    public void setHeatmapDimension(HeatmapDimension dim) {
        this.heatmapDimension = dim;
    }

    /**
     * The title
     */
    public String getTitle() {
        return (title == null) ? "" : title;
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


    public String getAnnotationPattern() {
        return annotationPattern;
    }

    public void setAnnotationPattern(String annotationPattern) {
        this.annotationPattern = annotationPattern;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String old = this.description;
        this.description = description;
        firePropertyChange(PROPERTY_DESCRIPTION, old, description);
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }

    public void setDescriptionUrl(String descriptionUrl) {
        String old = this.descriptionUrl;
        this.descriptionUrl = descriptionUrl;
        firePropertyChange(PROPERTY_DESCRIPTION_URL, old, descriptionUrl);
    }

    public String getValueUrl() {
        return valueUrl;
    }

    public void setValueUrl(String valueUrl) {
        String old = this.valueUrl;
        this.valueUrl = valueUrl;
        firePropertyChange(PROPERTY_VALUE_URL, old, valueUrl);
    }

    public void init(HeatmapDimension heatmapDimension) {
        this.setHeatmapDimension(heatmapDimension);

    }

    public void populateDetails(List<DetailsDecoration> details, String identifier, boolean selected) {
    }

    public abstract Function<String, String> getIdentifierTransform();

    /**
     * Override this method if you want that the sort by label related to this header
     * uses numeric sort.
     *
     * @return If the header contains numbers.
     */
    public boolean isNumeric() {
        return false;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean ascending) {
        this.sortAscending = ascending;
    }

    public String deriveTitleFromPattern() {
        String title = "";

        title = annotationPattern.replaceAll("[{}$]", "");

        return title;
    }
}

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

import org.gitools.heatmap.HeatmapDim;
import org.gitools.model.AbstractModel;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.gitools.utils.xml.adapter.FontXmlAdapter;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

/**
 * @noinspection ALL
 */
public abstract class HeatmapHeader extends AbstractModel
{

    private static final String TITLE_CHANGED = "title";
    public static final String SIZE_CHANGED = "size";
    public static final String VISIBLE_CHANGED = "visible";
    private static final String BG_COLOR_CHANGED = "bgColor";
    private static final String MARGIN_CHANGED = "margin";
    private static final String LABEL_VISIBLE_CHANGED = "labelVisible";
    static final String LABEL_FONT_CHANGED = "labelFont";
    private static final String LABEL_ROTATED_CHANGED = "labelRotated";
    private static final String LABEL_COLOR_CHANGED = "labelColor";
    private static final String LARGEST_LABEL_LENGTH_CHANGED = "largestLabelLength";

    /**
     * The title of the cluster set
     */
    private String title;

    /**
     * The height/width of the color band
     */
    int size;

    /**
     * Wether the cluster set is visible
     */
    private boolean visible;

    /* Background color*/
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    Color backgroundColor;

    /* Color band margin */ int margin;

    /**
     * Whether to show labels of each cluster
     */
    boolean labelVisible;

    /**
     * The font to use for labels
     */
    @XmlJavaTypeAdapter(FontXmlAdapter.class)
    Font font;

    /**
     * If false the label is painted along the color band,
     * otherwise the label is perpendicular to the color band
     */
    boolean labelRotated;

    /* If the header is referring to annotation from
    * the other dimension, the pattern is stored here*/
    @Nullable
    private String annotationPattern;

    private String[] annotationValues;

    /**
     * Tells the drawer how long the largest label is with current
     * font settings
     */

    int largestLabelLength;

    /**
     * Label foreground color
     */
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    Color labelColor;

    HeatmapHeader(HeatmapDim dim)
    {
        this.dim = dim;
        this.title = "";
        this.size = 100;
        this.visible = true;
        this.backgroundColor = Color.WHITE;
        font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
        margin = 1;
        labelRotated = false;
        labelVisible = false;
        labelColor = Color.BLACK;
        annotationPattern = null;
        annotationValues = new String[0];
    }

    @XmlTransient
    private HeatmapDim dim;

    public HeatmapDim getHeatmapDim()
    {
        return dim;
    }

    public void setHeatmapDim(HeatmapDim dim)
    {
        this.dim = dim;
    }

    /**
     * The title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * The title
     */
    public void setTitle(String title)
    {
        String old = this.title;
        this.title = title;
        firePropertyChange(TITLE_CHANGED, old, title);
    }

    /**
     * The height/width
     */
    public int getSize()
    {
        return size;
    }

    /**
     * The height/width
     */
    public void setSize(int size)
    {
        int old = this.size;
        this.size = size;
        firePropertyChange(SIZE_CHANGED, old, size);
    }

    /**
     * Wether the header is visible
     */
    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        boolean old = this.visible;
        this.visible = visible;
        firePropertyChange(VISIBLE_CHANGED, old, visible);
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(Color color)
    {
        Color old = this.backgroundColor;
        this.backgroundColor = color;
        firePropertyChange(BG_COLOR_CHANGED, old, color);
    }

    /* Color band margin */
    public int getMargin()
    {
        return margin;
    }

    /* Color band margin */
    public void setMargin(int margin)
    {
        int old = this.margin;
        this.margin = margin;
        firePropertyChange(MARGIN_CHANGED, old, margin);
    }

    /**
     * Whether to show labels of each value
     */
    public void setLabelVisible(boolean labelVisible)
    {
        boolean old = this.labelVisible;
        this.labelVisible = labelVisible;
        firePropertyChange(LABEL_VISIBLE_CHANGED, old, labelVisible);
    }

    public boolean isLabelVisible()
    {
        return this.labelVisible;
    }

    public Font getFont()
    {
        return font;
    }

    public void setFont(Font font)
    {
        this.font = font;
    }

    /**
     * The font to use for labels
     */
    public Font getLabelFont()
    {
        return font;
    }

    /**
     * The font to use for labels
     */
    public void setLabelFont(Font font)
    {
        Font old = this.font;
        this.font = font;
        updateLargestLabelLength(null);
        firePropertyChange(LABEL_FONT_CHANGED, old, font);
    }

    /**
     * If false the label is painted along the color band,
     * otherwise the label is perpendicular to the color band
     */
    public boolean isLabelRotated()
    {
        return labelRotated;
    }

    /**
     * If false the label is painted along the color band,
     * otherwise the label is perpendicular to the color band
     */
    public void setLabelRotated(boolean labelRotated)
    {
        boolean old = this.labelRotated;
        this.labelRotated = labelRotated;
        firePropertyChange(LABEL_ROTATED_CHANGED, old, labelRotated);
    }

    /**
     * Label foreground color
     */
    public Color getLabelColor()
    {
        return labelColor;
    }

    /**
     * Label foreground color
     */
    public void setLabelColor(Color labelColor)
    {
        Color old = this.labelColor;
        this.labelColor = labelColor;
        firePropertyChange(LABEL_COLOR_CHANGED, old, labelColor);
    }

    public int getLargestLabelLength()
    {
        return largestLabelLength;
    }

    void setLargestLabelLength(int largestLabelLength)
    {
        int old = this.largestLabelLength;
        this.largestLabelLength = largestLabelLength;
        firePropertyChange(LARGEST_LABEL_LENGTH_CHANGED, old, largestLabelLength);
    }

    @Nullable
    public String getAnnotationPattern()
    {
        return annotationPattern;
    }

    public void setAnnotationPattern(String annotationPattern)
    {
        this.annotationPattern = annotationPattern;
        //TODO: needs fireproperty change? - one time action
    }

    public String[] getAnnotationValues(boolean horizontal)
    {
        return annotationValues;
    }

    public void setAnnotationValues(String[] annotationValues)
    {
        this.annotationValues = annotationValues;
    }

    abstract protected void updateLargestLabelLength(Component component);

}

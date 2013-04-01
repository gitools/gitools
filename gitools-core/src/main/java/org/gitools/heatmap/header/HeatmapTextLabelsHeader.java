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
import org.gitools.matrix.model.AnnotationMatrix;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapTextLabelsHeader extends HeatmapHeader
{

    private static final long serialVersionUID = -2580139666999968074L;

    public static final String LABEL_PATTERN_CHANGED = "labelPatternChanged";
    public static final String LINK_NAME_CHANGED = "linkNameChanged";
    public static final String LINK_PATTERN_CHANGED = "linkPatternChanged";

    public enum LabelSource
    {
        ID, ANNOTATION, PATTERN
    }

    protected LabelSource labelSource;
    protected String labelAnnotation;
    protected String labelPattern;

    @Deprecated
    protected String linkName;
    @Deprecated
    protected String linkPattern;

    public HeatmapTextLabelsHeader()
    {
        this(null);
    }

    public HeatmapTextLabelsHeader(HeatmapDim hdim)
    {
        super(hdim);

        size = 80;
        labelColor = Color.BLACK;
        backgroundColor = Color.WHITE;
        font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
        AnnotationMatrix am = hdim != null ? hdim.getAnnotations() : null;
        if (am != null && am.getColumnCount() > 0)
        {
            labelSource = LabelSource.ANNOTATION;
            labelAnnotation = am.getColumnLabel(0);
        }
        else
        {
            labelSource = LabelSource.ID;
            labelAnnotation = "";
        }

        labelPattern = "${id}";
        linkName = "Google";
        linkPattern = "http://www.google.com/search?q=${url:id}";
    }

    @Override
    public String getTitle()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Text: ");
        switch (labelSource)
        {
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

    public Font getFont()
    {
        return font;
    }

    public void setFont(Font font)
    {
        Font old = this.font;
        this.font = font;
        firePropertyChange(LABEL_FONT_CHANGED, old, font);
    }

    @Override
    protected void updateLargestLabelLength(Component component)
    {
        this.largestLabelLength = 0;
    }

    public LabelSource getLabelSource()
    {
        return labelSource;
    }

    public void setLabelSource(LabelSource labelSource)
    {
        this.labelSource = labelSource;
    }

    public String getLabelAnnotation()
    {
        return labelAnnotation;
    }

    public void setLabelAnnotation(String labelAnnotation)
    {
        this.labelAnnotation = labelAnnotation;
    }

    public String getLabelPattern()
    {
        return labelPattern;
    }

    public void setLabelPattern(String pattern)
    {
        String old = this.labelPattern;
        this.labelPattern = pattern;
        firePropertyChange(LABEL_PATTERN_CHANGED, old, pattern);
    }

    @Deprecated
    public String getLinkName()
    {
        return linkName;
    }

    @Deprecated
    public void setLinkName(String linkName)
    {
        String old = this.linkName;
        this.linkName = linkName;
        firePropertyChange(LINK_NAME_CHANGED, old, linkName);
    }

    @Deprecated
    public String getLinkPattern()
    {
        return linkPattern;
    }

    @Deprecated
    public void setLinkPattern(String linkPattern)
    {
        String old = this.linkPattern;
        this.linkPattern = linkPattern;
        firePropertyChange(LINK_PATTERN_CHANGED, old, linkPattern);
    }
}
